package airhacks.zmcp.prompts.entity;

import org.json.JSONObject;

/**
 * The actual prompt content returned by the server. Matches the prompt signature.
 * https://modelcontextprotocol.io/specification/2025-03-26/server/prompts#prompt-content
 */
public record PromptInstance(PromptSignature signature, String description, Message message) {

    public static PromptInstance fromJson(String json) {
        var signature = PromptSignature.fromJson(json);
        var jsonObject = new JSONObject(json);
        var promptInstance = jsonObject.getJSONObject("prompt");
        var description = promptInstance.getString("description");
        var role = promptInstance.getString("role");
        var type = promptInstance.getString("type");
        var content = promptInstance.getString("content");
        var message = new Message(role, type, content);   
        return new PromptInstance(signature, description, message);
    }

    public boolean hasName(String name) {
        return signature.name().equals(name);
    }

    public String name(){
        return signature.name();
    }
}
