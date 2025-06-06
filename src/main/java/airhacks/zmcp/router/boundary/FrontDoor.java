package airhacks.zmcp.router.boundary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.resources.control.MessageSender;
import airhacks.zmcp.resources.entity.ResourceResponses;

import static airhacks.zmcp.router.entity.CoreMethods.INITIALIZE;
import static airhacks.zmcp.router.entity.CoreMethods.INITIALIZED;

import airhacks.zmcp.router.entity.CoreMethods;
import airhacks.zmcp.router.entity.MCPRequest;

public class FrontDoor {

    RequestHandler requestHandler;
    boolean isInitialized = false;
    MessageSender messageSender;

    public FrontDoor(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
        this.messageSender = new MessageSender();
    }

    public void start() throws IOException {
        Log.info("Starting StdioTransport");
        try (var isr = new InputStreamReader(System.in); var reader = new BufferedReader(isr)) {
            while (true) {
                try {
                    Log.info("Waiting for next message...");
                    var line = reader.readLine();
                    if (line == null) {
                        Log.info("Connection closed by client ");
                        break;
                    }
                    if (line.isBlank()) {
                        Log.info("Received blank line, skipping");
                        continue;
                    }
                    Log.request(line);
                    dispatchRequest(line);
                } catch (IOException e) {
                    Log.error("Error reading from input: " + e);
                    break;
                }
            }
        } finally {
            Log.info("StdioTransport stopped");
        }
    }

    void dispatchRequest(String request) {
        if (request == null) {
            Log.error("Received null request");
            messageSender.sendError(null, -32700, "Invalid request: null");
            return;
        }

        var jsonRequest = request.trim();
        if (!jsonRequest.startsWith("{") || !jsonRequest.endsWith("}")) {
            Log.error("Invalid JSON-RPC request format: " + jsonRequest);
            messageSender.sendError(null, -32700, "Invalid JSON-RPC request format");
            return;
        }

        var json = new JSONObject(jsonRequest);
        var method = json.optString("method", "");
        var id = json.optInt("id", -1);
        Log.info("Processing method: " + method + ", id: " + id);

        if (!isInitialized && !CoreMethods.INITIALIZE.isMethod(method)) {
            Log.error("Server not initialized, rejecting method: " + method);
            messageSender.sendError(id, -32002, "Server not initialized");
            return;
        }
        var mcpRequest = new MCPRequest(id,method, json);
        var coreMethodOptional = CoreMethods.fromString(method);
        if (coreMethodOptional.isEmpty()) {
            Log.error("Invalid method: " + method);
            messageSender.sendError(id, -32601, "Invalid method: " + method);
            return;
        }
        var coreMethod = coreMethodOptional.get();
        switch (coreMethod) {
            case INITIALIZE -> handleInitialize(id, json);
            case INITIALIZED -> handleInitialized();
            default -> this.requestHandler.handleRequest(mcpRequest);
        }

    }


    void handleInitialize(Integer id, JSONObject json) {
        Log.info("Handling initialize request: " + json);
        try {
            var params = json.getJSONObject("params");
            var protocolVersion = params.getString("protocolVersion");
            var clientInfo = params.getJSONObject("clientInfo");
            var clientName = clientInfo.getString("name");
            var clientVersion = clientInfo.getString("version");

            Log.info("Initializing with protocol version: %s, client: %s %s".formatted(
                    protocolVersion, clientName, clientVersion));

            if (!"2025-03-26".equals(protocolVersion)) {
                Log.error("Unsupported protocol version: " + protocolVersion);
            }

            var initializeResponse = ResourceResponses.initialize(id, protocolVersion);
            messageSender.send(initializeResponse);
            this.isInitialized = true;

        } catch (JSONException e) {
            Log.error("Error parsing initialize request: " + e.getMessage());
            messageSender.sendError(id, -32602, "Invalid params: " + e.getMessage());
        }
    }

    void handleInitialized() {
        Log.info("Client sent initialized notification");

    }
}
