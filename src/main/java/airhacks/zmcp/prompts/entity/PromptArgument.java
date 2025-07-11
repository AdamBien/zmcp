package airhacks.zmcp.prompts.entity;

import org.json.JSONObject;

/**
 * Example:
 * {
 * "jsonrpc": "2.0",
 * "id": 1,
 * "result": {
 * "prompts": [
 * {
 * "name": "code_review",
 * "description": "Asks the LLM to analyze code quality and suggest
 * improvements",
 * "arguments": [
 * {
 * "name": "code",
 * "description": "The code to review",
 * "required": true
 * }
 * ]
 * }
 * ],
 * "nextCursor": "next-page-cursor"
 * }
 * }
 */
public record PromptArgument(String name, String description, boolean required) {

    public JSONObject toJson() {
        var jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("description", description);
        jsonObject.put("required", required);
        return jsonObject;
    }

    public static PromptArgument fromJson(Object json) {
        if (json instanceof JSONObject jsonObject) {
            var name = jsonObject.getString("name");
            var description = jsonObject.getString("description");
            var required = jsonObject.getBoolean("required");
            return new PromptArgument(name, description, required);
        }
        throw new IllegalArgumentException("Object is not a JSONObject: " + json);
    }
}
