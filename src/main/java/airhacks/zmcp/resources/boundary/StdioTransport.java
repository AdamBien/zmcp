package airhacks.zmcp.resources.boundary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import airhacks.zmcp.resources.control.ResourceAcces;
import airhacks.zmcp.resources.entity.Resource;

public class StdioTransport {
    private final BufferedReader reader;
    private final PrintWriter writer;

    public StdioTransport() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.writer = new PrintWriter(System.out, true);
    }

    public void start() throws IOException {
        while (true) {
            var line = reader.readLine();
            if (line == null) {
                break;
            }
            handleRequest(line);
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

            switch (method) {
                case "initialize" -> handleInitialize(id);
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

    private void handleInitialize(Integer id) {
        var response = new HashMap<String, Object>();
        response.put("jsonrpc", "2.0");
        response.put("id", id);
        
        var result = new HashMap<String, Object>();
        var serverInfo = new HashMap<String, String>();
        serverInfo.put("name", "zmcp");
        serverInfo.put("version", "0.0.1");
        result.put("serverInfo", serverInfo);
        
        var capabilities = new HashMap<String, Object>();
        capabilities.put("resources", new HashMap<>());
        result.put("capabilities", capabilities);
        
        response.put("result", result);
        writer.println(toJson(response));
    }

    private void handleListResources(Integer id) {
        var resources = ResourceAcces.listResources();
        var resourcesJson = resources.stream()
            .map(Resource::toJson)
            .collect(Collectors.joining(","));
            
        var response = new HashMap<String, Object>();
        response.put("jsonrpc", "2.0");
        response.put("id", id);
        
        var result = new HashMap<String, Object>();
        result.put("resources", resources);
        response.put("result", result);
        
        writer.println(toJson(response));
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
        var response = new HashMap<String, Object>();
        response.put("jsonrpc", "2.0");
        response.put("id", id);
        
        var error = new HashMap<String, Object>();
        error.put("code", code);
        error.put("message", message);
        response.put("error", error);
        
        writer.println(toJson(response));
    }

    private String extractMethod(String jsonRequest) {
        // Simple extraction - in production, use a proper JSON parser
        var methodIndex = jsonRequest.indexOf("\"method\":\"");
        if (methodIndex == -1) return "";
        var start = methodIndex + 10;
        var end = jsonRequest.indexOf("\"", start);
        return jsonRequest.substring(start, end);
    }

    private Integer extractId(String jsonRequest) {
        // Simple extraction - in production, use a proper JSON parser
        var idIndex = jsonRequest.indexOf("\"id\":");
        if (idIndex == -1) return null;
        var start = idIndex + 5;
        var end = jsonRequest.indexOf(",", start);
        if (end == -1) end = jsonRequest.indexOf("}", start);
        return Integer.parseInt(jsonRequest.substring(start, end).trim());
    }

    private String toJson(Map<String, Object> map) {
        var sb = new StringBuilder();
        sb.append("{");
        var entries = map.entrySet().stream()
            .map(e -> "\"" + e.getKey() + "\":" + valueToJson(e.getValue()))
            .collect(Collectors.joining(","));
        sb.append(entries);
        sb.append("}");
        return sb.toString();
    }

    private String valueToJson(Object value) {
        if (value == null) return "null";
        if (value instanceof String) return "\"" + value + "\"";
        if (value instanceof Number) return value.toString();
        if (value instanceof Boolean) return value.toString();
        if (value instanceof Map) return toJson((Map<String, Object>) value);
        if (value instanceof Iterable) {
            var items = StreamSupport.stream(((Iterable<?>) value).spliterator(), false)
                .map(this::valueToJson)
                .collect(Collectors.joining(","));
            return "[" + items + "]";
        }
        return "\"" + value.toString() + "\"";
    }
} 