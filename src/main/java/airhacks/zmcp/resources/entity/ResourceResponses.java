package airhacks.zmcp.resources.entity;

import airhacks.App;
import airhacks.zmcp.resources.control.FileAccess.FileResourceContent;

public interface ResourceResponses {

    /**
     * https://modelcontextprotocol.io/specification/2025-03-26/basic/lifecycle#initialize
     * 
     * @param id
     * @return
     */
    static String initialize(Integer id, String protocolVersion, String capabilities) {

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
                         %s
                        }
                    }
                }"""
                .formatted(id, protocolVersion, App.VERSION, capabilities);
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
     * 
     * @param id
     * @param uri
     * @param mimeType
     * @param content
     * @return
     */
    static String readResource(int id, String uri, FileResourceContent resource) {
        var mimeType = resource.mimeType();
        var encodedContent = resource.encodedContent();
        var contentKey = resource.isBlob() ? "blob" : " text";
        return """
                {
                    "jsonrpc": "2.0",
                    "id": %d,
                    "result": {
                        "contents": [
                        {
                            "uri": "%s",
                            "mimeType": "%s",
                            "%s": %s
                        }
                        ]
                    }
                }""".formatted(id, uri, mimeType, contentKey, encodedContent);
    }


}