package airhacks.zmcp.tools.entity;

import java.util.List;

import airhacks.zmcp.jsonrpc.entity.JsonRPCResponses;
import airhacks.zmcp.tools.control.ToolInstance;

public interface ToolsResponses {

    static String listTools(int id, List<ToolInstance> tools) {
        var toolInstances = tools.stream()
                .map(ToolInstance::toJson)
                .toList();
        var toolInstancesArray = new org.json.JSONArray(toolInstances);
        var response = JsonRPCResponses.response(id);
        var result = new org.json.JSONObject();
        result.put("tools", toolInstancesArray);
        response.put("result", result);
        return response.toString();
    }

    /**
     * https://modelcontextprotocol.io/specification/2025-03-26/server/tools#calling-tools
     */
    static String toolCallTextContent(int id, ToolsResposeContent response) {
        var jsonObject = JsonRPCResponses.response(id);
        var result = new org.json.JSONObject();
        var contentArray = new org.json.JSONArray();
        contentArray.put(response.toJsonObject());
        result.put("content", contentArray);
        result.put("isError", response.error());
        jsonObject.put("result", result);
        return jsonObject.toString();
    }
}
