package airhacks.zmcp.tools.entity;

import org.json.JSONObject;

public record ToolsResposeContent(String type, String content,boolean error) {

    public static ToolsResposeContent text(String content) {
        return new ToolsResposeContent("text", content, false);
    }

    public static ToolsResposeContent error(String content) {
        return new ToolsResposeContent("text", content, true);
    }

    /**
     * https://modelcontextprotocol.io/specification/2025-03-26/server/tools#calling-tools
     */
    public String toJson() {
        var json = new JSONObject();
        json.put("type", type);
        json.put("text", content);
        return json.toString(1);
    }

}
