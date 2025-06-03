package airhacks.zmcp.resources.boundary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

import airhacks.zmcp.resources.control.ResourceAcces;
import airhacks.zmcp.resources.entity.Resource;

public class StdioTransport {
    private final BufferedReader reader;
    private final PrintWriter writer;
    private boolean isInitialized = false;

    public StdioTransport() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.writer = new PrintWriter(System.out, true);
    }

    public void start() throws IOException {
        while (true) {
            try {
                var line = reader.readLine();
                if (line == null) {
                    break;
                }
                if (!line.isBlank()) {
                    handleRequest(line);
                    writer.flush();
                }
            } catch (IOException e) {
                break;
            }
        }
    }

    private void handleRequest(String request) {
        try {
            // Parse the JSON-RPC request
            var jsonRequest = request.trim();
            if (!jsonRequest.startsWith("{") || !jsonRequest.endsWith("}")) {
                sendError(null, -32700, "Invalid JSON-RPC request format");
                return;
            }

            // Extract method and id
            var method = extractMethod(jsonRequest);
            var id = extractId(jsonRequest);

            // Only allow initialize before initialization
            if (!isInitialized && !"initialize".equals(method)) {
                sendError(id, -32002, "Server not initialized");
                return;
            }

            switch (method) {
                case "initialize" -> handleInitialize(id, jsonRequest);
                case "resources/list" -> handleListResources(id);
                case "resources/read" -> handleReadResource(id);
                case "resources/subscribe" -> handleSubscribe(id);
                case "resources/unsubscribe" -> handleUnsubscribe(id);
                default -> sendError(id, -32601, "Method not found: " + method);
            }
        } catch (Exception e) {
            sendError(null, -32603, e.getMessage());
        }
    }

    private void handleInitialize(Integer id, String jsonRequest) {
        var protocolVersion = extractValue(jsonRequest, "protocolVersion");
        var clientName = extractValue(jsonRequest, "name");
        var clientVersion = extractValue(jsonRequest, "version");

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
                        "resources": {}
                    }
                }
            }""".formatted(id).replaceAll("\\s+", "");
        writer.println(response);
        writer.flush();
        isInitialized = true;
    }

    private void handleListResources(Integer id) {
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
            }""".formatted(id, resourcesJson).replaceAll("\\s+", "");
        writer.println(response);
        writer.flush();
    }

    private void handleReadResource(Integer id) {
        sendError(id, -32601, "Method not implemented yet");
    }

    private void handleSubscribe(Integer id) {
        sendError(id, -32601, "Method not implemented yet");
    }

    private void handleUnsubscribe(Integer id) {
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
            }""".formatted(id == null ? "null" : id, code, message).replaceAll("\\s+", "");
        writer.println(response);
        writer.flush();
    }

    private String extractMethod(String jsonRequest) {
        var methodIndex = jsonRequest.indexOf("\"method\":\"");
        if (methodIndex == -1) return "";
        var start = methodIndex + 10;
        var end = jsonRequest.indexOf("\"", start);
        return jsonRequest.substring(start, end);
    }

    private Integer extractId(String jsonRequest) {
        var idIndex = jsonRequest.indexOf("\"id\":");
        if (idIndex == -1) return null;
        var start = idIndex + 5;
        var end = jsonRequest.indexOf(",", start);
        if (end == -1) end = jsonRequest.indexOf("}", start);
        return Integer.parseInt(jsonRequest.substring(start, end).trim());
    }

    private String extractValue(String jsonRequest, String key) {
        var keyIndex = jsonRequest.indexOf("\"" + key + "\":\"");
        if (keyIndex == -1) return "";
        var start = keyIndex + key.length() + 4;
        var end = jsonRequest.indexOf("\"", start);
        return jsonRequest.substring(start, end);
    }
} 