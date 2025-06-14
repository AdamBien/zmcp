package airhacks.zmcp.tools.entity;

import org.json.JSONObject;

public record ToolsResposeContent(String type, String content) {
    
    public static ToolsResposeContent text(String content) {
        var quotedContent = JSONObject.quote(content);
        return new ToolsResposeContent("text", quotedContent);
    }

    public String toJson() {
        return """
                {
                    "type": "%s",
                    "text": %s
                }
                """.formatted(type, content);
    }

}
