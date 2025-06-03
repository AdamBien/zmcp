package airhacks.zmcp.resources.entity;

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
            .formatted(id);
    }

    static String initialized() {
        return """
            {
                "jsonrpc": "2.0",
                "method": "initialized",
                "params": {}
            }""";
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
            .formatted(id, resourcesJson);
    }
} 