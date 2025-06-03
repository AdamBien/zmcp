package airhacks.zmcp.resources.boundary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.json.JSONException;

import airhacks.zmcp.jsonrpc.entity.ErrorResponses;
import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.resources.control.ResourceAcces;
import airhacks.zmcp.resources.entity.Resource;
import airhacks.zmcp.resources.entity.ResourceResponses;

public class StdioTransport {
    private final BufferedReader reader;
    private final PrintWriter writer;
    private boolean isInitialized = false;

    public StdioTransport() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.writer = new PrintWriter(System.out, true);
    }

    public void start() throws IOException {
        Log.info("Starting StdioTransport");
        try {
            while (true) {
                try {
                    var line = reader.readLine();
                    if (line == null) {
                        Log.info("Connection closed by client");
                        break;
                    }
                    if (line.isBlank()) {
                        continue;
                    }
                    Log.info("Received request: " + line);
                    handleRequest(line);
                    writer.flush();
                } catch (IOException e) {
                    Log.error("Error reading from input: " + e.getMessage());
                    break;
                }
            }
        } finally {
            Log.info("StdioTransport stopped");
            try {
                reader.close();
            } catch (IOException e) {
                Log.error("Error closing reader: " + e.getMessage());
            }
            writer.close();
        }
    }

    private void handleRequest(String request) {
        Log.info("Handling request: " + request);
        try {
            if (request == null) {
                Log.error("Received null request");
                sendError(null, -32700, "Invalid request: null");
                return;
            }

            // Parse the JSON-RPC request
            var jsonRequest = request.trim();
            if (!jsonRequest.startsWith("{") || !jsonRequest.endsWith("}")) {
                Log.error("Invalid JSON-RPC request format: " + jsonRequest);
                sendError(null, -32700, "Invalid JSON-RPC request format");
                return;
            }

            var json = new JSONObject(jsonRequest);
            var method = json.optString("method", "");
            var id = json.optInt("id", -1);
            Log.info("Processing request - method: " + method + ", id: " + id);

            // Only allow initialize before initialization
            if (!isInitialized && !"initialize".equals(method)) {
                Log.error("Server not initialized, rejecting method: " + method);
                sendError(id, -32002, "Server not initialized");
                return;
            }

            switch (method) {
                case "initialize" -> handleInitialize(id, json);
                case "initialized" -> handleInitialized();
                case "resources/list" -> handleListResources(id);
                case "resources/read" -> handleReadResource(id);
                case "resources/subscribe" -> handleSubscribe(id);
                case "resources/unsubscribe" -> handleUnsubscribe(id);
                default -> {
                    Log.error("Method not found: " + method);
                    sendError(id, -32601, "Method not found: " + method);
                }
            }
        } catch (JSONException e) {
            Log.error("Error parsing JSON request: " + e.getMessage());
            sendError(null, -32700, "Invalid JSON-RPC request format");
        } catch (Exception e) {
            Log.error("Error handling request: " + e.getMessage());
            sendError(null, -32603, "Internal error: " + e.getMessage());
        }
    }

    private void handleInitialize(Integer id, JSONObject json) {
        try {
            var params = json.getJSONObject("params");
            var protocolVersion = params.getString("protocolVersion");
            var clientInfo = params.getJSONObject("clientInfo");
            var clientName = clientInfo.getString("name");
            var clientVersion = clientInfo.getString("version");
            
            Log.info("Initializing with protocol version: %s, client: %s %s".formatted(
                protocolVersion, clientName, clientVersion));

            // Validate protocol version
            if (!"2025-03-26".equals(protocolVersion)) {
                Log.error("Unsupported protocol version: " + protocolVersion);
            }

            // Send initialize response
            var initializeResponse = ResourceResponses.initialize(id);
            Log.info("Sending initialize response: " + initializeResponse);
            this.write(initializeResponse);

            // Set initialized flag
            isInitialized = true;

            // Send initialized notification
            var initializedNotification = ResourceResponses.initialized();
            Log.info("Sending initialized notification: " + initializedNotification);
            this.write(initializedNotification);
        } catch (JSONException e) {
            Log.error("Error parsing initialize request: " + e.getMessage());
            sendError(id, -32602, "Invalid params: " + e.getMessage());
        }
    }

    private void handleInitialized() {
        Log.info("Client sent initialized notification");
    }

    private void handleListResources(Integer id) {
        Log.info("Handling list resources request");
        var resources = ResourceAcces.listResources();
        var resourcesJson = resources.stream()
            .map(Resource::toJson)
            .collect(Collectors.joining(","));
            
        Log.info("Sending list resources response");
        this.write(ResourceResponses.listResources(id, resourcesJson));
    }

    private void handleReadResource(Integer id) {
        Log.info("Handling read resource request");
        sendError(id, -32601, "Method not implemented yet");
    }

    private void handleSubscribe(Integer id) {
        Log.info("Handling subscribe request");
        sendError(id, -32601, "Method not implemented yet");
    }

    private void handleUnsubscribe(Integer id) {
        Log.info("Handling unsubscribe request");
        sendError(id, -32601, "Method not implemented yet");
    }

    private void sendError(Integer id, int code, String message) {
        Log.info("Sending error response");
        this.write(ErrorResponses.error(id, code, message));
    }

    void write(String message) {
        writer.println(message);
        writer.flush();
    }
}