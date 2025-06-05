package airhacks.zmcp.resources.boundary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;

import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.resources.control.MessageSender;
import airhacks.zmcp.resources.control.FileAccess;
import airhacks.zmcp.resources.entity.Resource;
import airhacks.zmcp.resources.entity.ResourceResponses;
import airhacks.zmcp.resources.entity.ResourcesMethods;

/**
 * https://modelcontextprotocol.io/specification/2025-03-26/basic/lifecycle
 */
public class ResourcesSTDIOProtocol {
    boolean isInitialized = false;
    MessageSender messageSender;
    FileAccess fileAccess;
    
    public ResourcesSTDIOProtocol(String rootFolder) {
        this.fileAccess = new FileAccess(rootFolder);
        this.messageSender = new MessageSender();
    }

    public void start() throws IOException {
        Log.info("Starting StdioTransport");
        try(var isr = new InputStreamReader(System.in);var reader = new BufferedReader(isr)) {
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
                    handleRequest(line);
                } catch (IOException e) {
                    Log.error("Error reading from input: " + e);
                    break;
                }
            }
        } finally {
            Log.info("StdioTransport stopped");
        }
    }

    void handleRequest(String request) {
        Log.info("Processing request: " + request);
        try {
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

            if (!isInitialized && !ResourcesMethods.INITIALIZE.isMethod(method)) {
                Log.error("Server not initialized, rejecting method: " + method);
                messageSender.sendError(id, -32002, "Server not initialized");
                return;
            }

            var optionalProtocol = ResourcesMethods.fromString(method);
            if (optionalProtocol.isEmpty()) {
                Log.error("Method not found: " + method);
                messageSender.sendError(id, -32601, "Method not found: " + method);
                return;
            }
            var protocol = optionalProtocol.get();
            switch (protocol) {
                case INITIALIZE -> handleInitialize(id, json);
                case INITIALIZED -> handleInitialized();
                case RESOURCES_LIST -> handleListResources(id);
                case NOTIFICATIONS_INITIALIZED -> handleNotificationsInitialized();
                case NOTIFICATIONS_CANCELLED -> handleNotificationsCancelled();
                case RESOURCES_READ -> handleReadResource(id, json);
                case SUBSCRIBE -> handleSubscribe(id);
                case UNSUBSCRIBE -> handleUnsubscribe(id);
            }
        } catch (JSONException e) {
            Log.error("Error parsing JSON request: " + e);
            messageSender.sendError(null, -32700, "Invalid JSON-RPC request format");
        } catch (Exception e) {
            Log.error("Error handling request: " + e);
            messageSender.sendError(null, -32603, "Internal error: " + e.getMessage());
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

            var initializeResponse = ResourceResponses.initialize(id);
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

    void handleNotificationsInitialized() {
        Log.info("Client sent notifications initialized notification");
    }

    void handleNotificationsCancelled() {
        Log.info("Client sent notifications cancelled notification");
    }

    void handleListResources(Integer id) {
        Log.info("Handling list resources request");
        var resources = this.fileAccess.listResources();
        var resourcesJson = resources.stream()
                .map(Resource::toJson)
                .collect(Collectors.joining(","));

        Log.info("Sending list resources response");
        messageSender.send(ResourceResponses.listResources(id, resourcesJson));
    }

    /**
     * https://modelcontextprotocol.io/specification/2025-03-26/server/resources#reading-resources
     * {
     * "jsonrpc": "2.0",
     * "id": 2,
     * "method": "resources/read",
     * "params": {
     * "uri": "file:///project/src/main.rs"
     * }
     * }
     * 
     * @param id
     * @param json
     */
    void handleReadResource(Integer id, JSONObject json) {
        Log.info("Handling read resource request");
        var params = json.getJSONObject("params");
        var uri = params.getString("uri");
        var content = this.fileAccess.readResource(uri);
        var response = ResourceResponses.readResource(id, uri, "text/plain", content);
        messageSender.send(response);
    }


    void handleSubscribe(Integer id) {
        Log.info("Handling subscribe request");
        messageSender.methodNotImplementedYet(id);
    }

    void handleUnsubscribe(Integer id) {
        Log.info("Handling unsubscribe request");
        messageSender.methodNotImplementedYet(id);
    }



}