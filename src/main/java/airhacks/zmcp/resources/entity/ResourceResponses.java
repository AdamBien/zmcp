package airhacks.zmcp.resources.entity;

import org.json.JSONObject;

import airhacks.App;

public interface ResourceResponses {

    /**
     * https://modelcontextprotocol.io/specification/2025-03-26/basic/lifecycle#initialize
     * @param id
     * @return
     */
    static String initialize(Integer id, String protocolVersion) {
        return """
                {
                    "jsonrpc": "2.0",
                    "id": %d,
                    "result": {
                        "protocolVersion": "%s",
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
                .formatted(id, protocolVersion,App.VERSION);
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

    /**
     * https://modelcontextprotocol.io/specification/2025-03-26/server/resources#reading-resources
     * @param id
     * @param uri
     * @param mimeType
     * @param content
     * @return
     */
    static String readResource(int id, String uri, String mimeType, String content) {
        var quotedContent = JSONObject.quote(content);
        return """
            {
                "jsonrpc": "2.0",
                "id": %d,
                "result": {
                    "contents": [
                    {
                        "uri": "%s",
                        "mimeType": "%s",
                        "text": %s
                    }
                    ]
                }
            }""".formatted(id,uri,mimeType,quotedContent);
    }
}