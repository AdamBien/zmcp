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

    public PromptSignature fromJson(String json) {
        var jsonObject = new JSONObject(json);
        var name = jsonObject.getString("name");
        var description = jsonObject.getString("description");
        var arguments = jsonObject.getJSONArray("arguments");
        var argumentsList = arguments
                .toList()
                .stream()
                .map(PromptArgument::fromJson)
                .toList();
        return new PromptSignature(name, description, argumentsList);
    }
}
