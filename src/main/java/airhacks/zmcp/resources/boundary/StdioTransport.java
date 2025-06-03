package airhacks.zmcp.resources.boundary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

import airhacks.zmcp.resources.control.ResourceAcces;
import airhacks.zmcp.resources.entity.Resource;
import airhacks.zmcp.log.boundary.Log;

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
                    if (!line.isBlank()) {
                        Log.info("Received request: " + line);
                        handleRequest(line);
                        writer.flush();
                    }
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

            // Extract method and id
            var method = extractMethod(jsonRequest);
            var id = extractId(jsonRequest);
            Log.info("Processing request - method: " + method + ", id: " + id);

            // Only allow initialize before initialization
            if (!isInitialized && !"initialize".equals(method)) {
                Log.error("Server not initialized, rejecting method: " + method);
                sendError(id, -32002, "Server not initialized");
                return;
            }

            switch (method) {
                case "initialize" -> handleInitialize(id, jsonRequest);
                case "resources/list" -> handleListResources(id);
                case "resources/read" -> handleReadResource(id);
                case "resources/subscribe" -> handleSubscribe(id);
                case "resources/unsubscribe" -> handleUnsubscribe(id);
                default -> {
                    Log.error("Method not found: " + method);
                    sendError(id, -32601, "Method not found: " + method);
                }
            }
        } catch (Exception e) {
            Log.error("Error handling request: " + e.getMessage());
            sendError(null, -32603, "Internal error: " + e.getMessage());
        }
    }

    private void handleInitialize(Integer id, String jsonRequest) {
        var protocolVersion = extractValue(jsonRequest, "protocolVersion");
        var clientName = extractValue(jsonRequest, "name");
        var clientVersion = extractValue(jsonRequest, "version");
        Log.info("Initializing with protocol version: %s, client: %s %s".formatted(protocolVersion, clientName, clientVersion));

        var response = """
            {
                "jsonrpc": "2.0",
                "id": %d,
                "result": {
                    "serverInfo": {
                        "name": "zmcp",
                        "version": "0.0.1"
                    },
                    "capabilities": {
                        "resources": {
                            "list": true,
                            "read": true,
                            "subscribe": true,
                            "unsubscribe": true,
                            "maxSize": 1048576,
                            "supportedMimeTypes": [
                                "text/plain",
                                "text/markdown",
                                "application/json"
                            ]
                        },
                        "roots": {
                            "list": true,
                            "listChanged": true
                        },
                        "sampling": {
                            "enabled": true
                        }
                    }
                }
            }"""
            .formatted(id);
        Log.info("Sending initialize response");
        writer.println(response);
        writer.flush();
        isInitialized = true;
    }

    private void handleListResources(Integer id) {
        Log.info("Handling list resources request");
        var resources = ResourceAcces.listResources();
        var resourcesJson = resources.stream()
            .map(Resource::toJson)
            .collect(Collectors.joining(","));
            
        var response = """
            {
                "jsonrpc": "2.0",
                "id": %d,
                "result": {
                    "resources": [%s]
                }
            }"""
            .formatted(id, resourcesJson)
            .replaceAll("\\s+", "");
        Log.info("Sending list resources response: " + response);
        writer.println(response);
        writer.flush();
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
        var response = """
            {
                "jsonrpc": "2.0",
                "id": %s,
                "error": {
                    "code": %d,
                    "message": "%s"
                }
            }"""
            .formatted(id == null ? "null" : id, code, message)
            .replaceAll("\\s+", "");
        Log.info("Sending error response: " + response);
        writer.println(response);
        writer.flush();
    }

    private String extractMethod(String jsonRequest) {
        var methodIndex = jsonRequest.indexOf("\"method\":\"");
        if (methodIndex == -1) {
            Log.error("Method not found in request");
            return "";
        }
        var start = methodIndex + 10;
        var end = jsonRequest.indexOf("\"", start);
        if (end == -1) {
            Log.error("Invalid method format in request");
            return "";
        }
        return jsonRequest.substring(start, end);
    }

    private Integer extractId(String jsonRequest) {
        var idIndex = jsonRequest.indexOf("\"id\":");
        if (idIndex == -1) {
            Log.error("ID not found in request");
            return null;
        }
        var start = idIndex + 5;
        var end = jsonRequest.indexOf(",", start);
        if (end == -1) {
            end = jsonRequest.indexOf("}", start);
        }
        if (end == -1) {
            Log.error("Invalid ID format in request");
            return null;
        }
        try {
            return Integer.parseInt(jsonRequest.substring(start, end).trim());
        } catch (NumberFormatException e) {
            Log.error("Invalid ID format: " + jsonRequest.substring(start, end));
            return null;
        }
    }

    private String extractValue(String jsonRequest, String key) {
        var keyIndex = jsonRequest.indexOf("\"" + key + "\":\"");
        if (keyIndex == -1) {
            Log.error("Key not found in request: " + key);
            return "";
        }
        var start = keyIndex + key.length() + 4;
        var end = jsonRequest.indexOf("\"", start);
        if (end == -1) {
            Log.error("Invalid value format for key: " + key);
            return "";
        }
        return jsonRequest.substring(start, end);
    }
}