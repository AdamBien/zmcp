package airhacks.zmcp.resources.entity;

import airhacks.App;
import airhacks.zmcp.jsonrpc.entity.JsonRPCResponses;
import airhacks.zmcp.resources.control.FileAccess.FileResourceContent;

public interface ResourceResponses {

    /**
     * https://modelcontextprotocol.io/specification/2025-03-26/basic/lifecycle#initialize
     * 
     * @param id
     * @return
     */
    static String initialize(Integer id, String protocolVersion, String capabilities) {

        var header = JsonRPCResponses.header(id);
        return """
                {
                    %s,
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
                .formatted(header, protocolVersion, App.VERSION, capabilities);
    }

    static String listResources(Integer id, String resourcesJson) {
        var header = JsonRPCResponses.header(id);
        return """
                {
                    %s,
                    "result": {
                        "resources": [%s]
                    }
                }"""
                .formatted(header, resourcesJson);
    }

    static String ping(int id) {
        var header = JsonRPCResponses.header(id);

        return """
                {
                    %s,
                    "method": "ping"
                }"""
                .formatted(header);
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
        var header = JsonRPCResponses.header(id);
        return """
                {
                    %s,
                    "result": {
                        "contents": [
                        {
                            "uri": "%s",
                            "mimeType": "%s",
                            "%s": %s
                        }
                        ]
                    }
                }""".formatted(header, uri, mimeType, contentKey, encodedContent);
    }


}