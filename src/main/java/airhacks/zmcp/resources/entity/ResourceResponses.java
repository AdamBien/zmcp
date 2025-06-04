package airhacks.zmcp.resources.entity;

import airhacks.App;

public interface ResourceResponses {
    static String initialize(Integer id) {
        return """
                {
                    "jsonrpc": "2.0",
                    "id": %d,
                    "result": {
                        "protocolVersion": "2025-03-26",
                        "serverInfo": {
                            "name": "zmcp",
                            "version":"%s"
                        },
                        "capabilities": {
                            "resources": {
                                "list": true,
                                "read": true,
                                "supportedMimeTypes": [
                                    "text/plain",
                                    "text/markdown",
                                    "application/json"
                                ]
                            },
                            "roots": {
                                "list": true,
                                "listChanged": true
                            }
                        }
                    }
                }"""
                .formatted(id,App.VERSION);
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

    static String ping(int id) {
        return """
                {
                    "jsonrpc": "2.0",
                    "id": "%d",
                    "method": "ping"
                }"""
                .formatted(id);
    }
}