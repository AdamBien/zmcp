package airhacks.zmcp.prompts.entity;

import org.json.JSONObject;

public record Message(String role, String type,String content) {

    public Message{
        content = JSONObject.quote(content);
    }
    
    public static Message text(String content) {
        return new Message("user","text",content);
    }

    public String toJson() {
        return """
                {
                    "role": "%s",
                    "content": {
                        "type": "%s",
                        "text": %s
                    }
                }
                """.formatted(role, type, content);
    }
}
