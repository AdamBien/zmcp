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
        result.put("contents", new JSONArray("""
                [{
                    "uri": "%s",
                    "mimeType": "%s",
                    "%s": %s
                }]
                """.formatted(uri, resource.mimeType(), 
                             resource.isBlob() ? "blob" : "text", 
                             resource.encodedContent())));
        response.put("result", result);
        return response.toString();
    }


}