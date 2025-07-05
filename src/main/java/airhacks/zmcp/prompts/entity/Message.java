package airhacks.zmcp.prompts.entity;

import org.json.JSONObject;

public record Message(String role, String type, String content) {



    public static Message text(String content) {
        return new Message("user", "text", content);
    }

    public JSONObject toJson() {
        var json = new JSONObject();
        json.put("role", role);
        var content = new JSONObject();
        content.put("type", type);
        content.put("text", this.content);
        json.put("content", content);
        return json;
    }
}
