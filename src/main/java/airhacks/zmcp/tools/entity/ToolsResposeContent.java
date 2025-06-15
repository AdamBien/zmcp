package airhacks.zmcp.tools.entity;

import org.json.JSONObject;

public record ToolsResposeContent(String type, String content,boolean error) {

    public ToolsResposeContent{
        content = JSONObject.quote(content);
    }
    
    public static ToolsResposeContent text(String content) {
        return new ToolsResposeContent("text", content, false);
    }

    public static ToolsResposeContent error(String content) {
        return new ToolsResposeContent("text", content, true);
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
