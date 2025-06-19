package airhacks.zmcp.prompts.entity;

import org.json.JSONObject;

public record Prompt(String description, Message message) {

    public static Prompt fromJson(String json) {
        var jsonObject = new JSONObject(json);
        var description = jsonObject.getString("description");
        var role = jsonObject.getString("role");
        var type = jsonObject.getString("type");
        var content = jsonObject.getString("content");
        var message = new Message(role, type, content);   
        return new Prompt(description, message);
    }
}
