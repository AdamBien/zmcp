package airhacks.zmcp.resources.entity;

import java.util.List;
import java.util.Map;

public interface ResourceResponses {
    static String initialize(Integer id) {
        return """
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
    }

    static String initialized() {
        return """
            {
                "jsonrpc": "2.0",
                "method": "initialized",
                "params": {}
            }"""
            .replaceAll("\\s+", "");
    }

    static String listResources(Integer id, String resourcesJson) {
        return """
            {
                "jsonrpc": "2.0",
                "id": %d,
                "result": {
                    "resources": [%s]
                }
            }"""
            .formatted(id, resourcesJson)
            .replaceAll("\\s+", "");
    }

    static String error(Integer id, int code, String message) {
        return error(id, code, message, null);
    }

    static String error(Integer id, int code, String message, Map<String, Object> data) {
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

        return """
            {
                "jsonrpc": "2.0",
                "id": %s,
                "error": %s
            }"""
            .formatted(id == null ? "null" : id, error)
            .replaceAll("\\s+", "");
    }
} 