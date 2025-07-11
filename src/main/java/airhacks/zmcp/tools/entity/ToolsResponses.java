package airhacks.zmcp.tools.entity;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import airhacks.zmcp.jsonrpc.entity.JsonRPCResponses;
import airhacks.zmcp.tools.control.ToolInstance;

public interface ToolsResponses {

    static JSONObject listTools(int id, List<ToolInstance> tools) {
        var toolInstances = tools.stream()
                .map(ToolInstance::toJson)
                .toList();
        var toolInstancesArray = new JSONArray(toolInstances);
        var response = JsonRPCResponses.response(id);
        var result = new JSONObject();
        result.put("tools", toolInstancesArray);
        response.put("result", result);
        return response;
    }

    /**
     * https://modelcontextprotocol.io/specification/2025-03-26/server/tools#calling-tools
     */
    static JSONObject toolCallTextContent(int id, ToolsResposeContent response) {
        var jsonObject = JsonRPCResponses.response(id);
        var result = new JSONObject();
        var contentArray = new JSONArray();
        contentArray.put(response.toJsonObject());
        result.put("content", contentArray);
        result.put("isError", response.error());
        jsonObject.put("result", result);
        return jsonObject;
    }
}
