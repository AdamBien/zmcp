package airhacks.zmcp.resources.boundary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Arrays;

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
        } catch (Exception e) {
            Log.error("Error handling request: " + e.getMessage());
            sendError(null, -32603, "Internal error: " + e.getMessage());
        }
    }

    private void handleInitialize(Integer id, String jsonRequest) {
        var protocolVersion = extractValue(jsonRequest, "protocolVersion");
        var clientName = extractValue(jsonRequest, "clientInfo.name");
        var clientVersion = extractValue(jsonRequest, "clientInfo.version");
        Log.info("Initializing with protocol version: %s, client: %s %s".formatted(protocolVersion, clientName, clientVersion));

        // Validate protocol version
        if (!"2025-03-26".equals(protocolVersion)) {
            sendError(id, -32602, "Unsupported protocol version", Map.of(
                "supported", List.of("2025-03-26"),
                "requested", protocolVersion
            ));
            return;
        }

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
            .formatted(id)
            .replaceAll("\\s+", "");
        Log.info("Sending initialize response");
        writer.println(response);
        writer.flush();
        isInitialized = true;

        // Send initialized notification
        var initialized = """
            {
                "jsonrpc": "2.0",
                "method": "initialized",
                "params": {}
            }"""
            .replaceAll("\\s+", "");
        Log.info("Sending initialized notification");
        writer.println(initialized);
        writer.flush();
    }

    private void handleInitialized() {
        Log.info("Client sent initialized notification");
        // No response needed for notifications
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
        Log.info("Sending list resources response");
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
        sendError(id, code, message, null);
    }

    private void sendError(Integer id, int code, String message, Map<String, Object> data) {
        var error = new StringBuilder();
        error.append("{\"code\":").append(code).append(",\"message\":\"").append(message).append("\"");
        if (data != null) {
            error.append(",\"data\":{");
            var entries = data.entrySet().iterator();
            while (entries.hasNext()) {
                var entry = entries.next();
                error.append("\"").append(entry.getKey()).append("\":");
                if (entry.getValue() instanceof String) {
                    error.append("\"").append(entry.getValue()).append("\"");
                } else if (entry.getValue() instanceof List) {
                    var list = (List<?>) entry.getValue();
                    error.append("[");
                    for (int i = 0; i < list.size(); i++) {
                        if (i > 0) error.append(",");
                        error.append("\"").append(list.get(i)).append("\"");
                    }
                    error.append("]");
                } else {
                    error.append(entry.getValue());
                }
                if (entries.hasNext()) {
                    error.append(",");
                }
            }
            error.append("}");
        }
        error.append("}");

        var response = """
            {
                "jsonrpc": "2.0",
                "id": %s,
                "error": %s
            }"""
            .formatted(id == null ? "null" : id, error)
            .replaceAll("\\s+", "");
        Log.info("Sending error response");
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
        var parts = key.split("\\.");
        var currentKey = parts[0];
        var keyIndex = jsonRequest.indexOf("\"" + currentKey + "\":");
        if (keyIndex == -1) {
            Log.error("Key not found in request: " + currentKey);
            return "";
        }
        var start = keyIndex + currentKey.length() + 3;
        var end = jsonRequest.indexOf(",", start);
        if (end == -1) {
            end = jsonRequest.indexOf("}", start);
        }
        if (end == -1) {
            Log.error("Invalid value format for key: " + currentKey);
            return "";
        }
        var value = jsonRequest.substring(start, end).trim();
        
        // Handle nested keys
        if (parts.length > 1) {
            var remainingKey = String.join(".", Arrays.copyOfRange(parts, 1, parts.length));
            return extractValue(value, remainingKey);
        }
        
        // Remove quotes if present
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }
        return value;
    }
}