package airhacks.zmcp.prompts.entity;

import java.util.List;

import org.json.JSONObject;
/**
 * The prompt signature. Used for listing prompts.
 * https://modelcontextprotocol.io/specification/2025-03-26/server/prompts#listing-prompts
 */
public record PromptSignature(String name, String description, List<PromptArgument> arguments) {

    /**
     * 
     * https://modelcontextprotocol.io/specification/2025-03-26/server/prompts#listing-prompts
     */

    public String toJson() {
        var argumentsJson = arguments.stream()
        .map(PromptArgument::toJson)
        .toList();
        var jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("description", description);
        jsonObject.put("arguments", argumentsJson);
        return jsonObject.toString();
    }

    public static PromptSignature fromJson(String json) {
        var jsonObject = new JSONObject(json);
        var signature = jsonObject.getJSONObject("signature");
        var name = signature.getString("name");
        var description = signature.getString("description");
        var arguments = signature.getJSONArray("arguments");
        var argumentsList = arguments
                .toList()
                .stream()
                .map(PromptArgument::fromJson)
                .toList();
        return new PromptSignature(name, description, argumentsList);
    }
}
