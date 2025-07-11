package airhacks.zmcp.router.boundary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;

import airhacks.zmcp.base.boundary.CoreSTDIOProtocol;
import airhacks.zmcp.base.control.MessageSender;
import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.router.entity.Capability;
import airhacks.zmcp.router.entity.MCPRequest;

public class FrontDoor {

    List<RequestHandler> requestHandlers;
    MessageSender messageSender;

    public FrontDoor(List<RequestHandler> requestHandlers) {
        this.requestHandlers = new ArrayList<>(requestHandlers);
        this.messageSender = new MessageSender();
        var capabilities = this.capabilities();
        this.requestHandlers.add(new CoreSTDIOProtocol(capabilities));
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

        var mcpRequest = new MCPRequest(id, method, json);
        var requestHandled = this.requestHandlers
                .stream()
                .map(requestHandler -> requestHandler.handleRequest(mcpRequest))
                .filter(Boolean::booleanValue)
                .findAny()
                .isPresent();
        if (!requestHandled) {
            Log.error("No request handler found for method: " + method);
            messageSender.sendError(id, -32601, "Method not found: " + method);
        }
    }

    List<Capability> capabilities() {
        return this.requestHandlers.stream()
                .map(RequestHandler::capability)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

}
