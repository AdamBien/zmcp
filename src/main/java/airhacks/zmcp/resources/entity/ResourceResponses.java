package airhacks.zmcp.resources.entity;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import airhacks.App;
import airhacks.zmcp.jsonrpc.entity.JsonRPCResponses;
import airhacks.zmcp.resources.control.FileAccess.FileResourceContent;
import airhacks.zmcp.router.entity.Capability;

public interface ResourceResponses {

    /**
     * https://modelcontextprotocol.io/specification/2025-03-26/basic/lifecycle#initialize
     * 
     * @param id
     * @return
     */
    static JSONObject initialize(Integer id, String protocolVersion, List<Capability> capabilities) {
        var response = JsonRPCResponses.response(id);
        
        var result = new JSONObject();
        result.put("protocolVersion", protocolVersion);
        result.put("capabilities", capabilities(capabilities));
        
        var serverInfo = new JSONObject();
        serverInfo.put("name", "zmcp");
        serverInfo.put("version", App.VERSION);
        result.put("serverInfo", serverInfo);
        
        response.put("result", result);
        return response;
    }

    static JSONObject capabilities(List<Capability> capabilities) {
        var capabilitiesJson = new JSONObject();
        capabilities.forEach(capability -> {
            var capabilityObject = new JSONObject();
            capabilityObject.put("listChanged", capability.listChanged());
            capabilitiesJson.put(capability.name(), capabilityObject);
        });
        return capabilitiesJson;
    }

    static String listResources(Integer id, String resourcesJson) {
        var response = JsonRPCResponses.response(id);
        var result = new JSONObject();
        result.put("resources", new JSONArray(resourcesJson));
        response.put("result", result);
        return response.toString();
    }

    static String ping(int id) {
        var response = JsonRPCResponses.response(id);
        response.put("method", "ping");
        return response.toString();
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
        var response = JsonRPCResponses.response(id);
        var result = new JSONObject();
        var contents = new JSONArray();
        
        var content = new JSONObject();
        content.put("uri", uri);
        content.put("mimeType", resource.mimeType());
        
        var contentKey = resource.isBlob() ? "blob" : "text";
        var encodedContent = resource.encodedContent();
        
        if (resource.isBlob()) {
            content.put(contentKey, encodedContent);
        } else {
            // For text content, encodedContent is already a quoted JSON string
            // We need to parse it to get the actual string value
            var jsonString = """
                    {"temp": %s}
                    """.formatted(encodedContent);
            var temp = new JSONObject(jsonString);
            content.put(contentKey, temp.getString("temp"));
        }
        
        contents.put(content);
        result.put("contents", contents);
        response.put("result", result);
        
        return response.toString();
    }


}