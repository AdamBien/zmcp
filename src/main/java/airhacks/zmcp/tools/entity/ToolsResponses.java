package airhacks.zmcp.tools.entity;

import java.util.List;
import java.util.stream.Collectors;

import airhacks.zmcp.jsonrpc.entity.JsonRPCResponses;
import airhacks.zmcp.tools.control.ToolInstance;

public interface ToolsResponses {

    static String listTools(int id, List<ToolInstance> tools) {
        var toolJson = tools.stream()
                .map(ToolInstance::toJson)
                .collect(Collectors.joining(","));
        var header = JsonRPCResponses.response(id);
        return """
                    {
                    %s,
                    "result": {
                        "tools": [
                        %s
                        ]
                    }
                }
                 """.formatted(header, toolJson);
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
