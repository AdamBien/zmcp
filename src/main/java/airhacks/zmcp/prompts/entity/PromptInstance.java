package airhacks.zmcp.prompts.entity;

import org.json.JSONObject;

/**
 * The actual prompt content returned by the server. Matches the prompt signature.
 * https://modelcontextprotocol.io/specification/2025-03-26/server/prompts#prompt-content
 */
public record PromptInstance(String description, Message message) {

    public static PromptInstance fromJson(String json) {
        var jsonObject = new JSONObject(json);
        var description = jsonObject.getString("description");
        var role = jsonObject.getString("role");
        var type = jsonObject.getString("type");
        var content = jsonObject.getString("content");
        var message = new Message(role, type, content);   
        return new PromptInstance(description, message);
    }
}
