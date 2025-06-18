package airhacks.zmcp.prompts.entity;

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

    public String toJson() {
        return """
                {
                    "name": "%s",
                    "description": "%s",
                    "required": %b
                }
                """.formatted(name, description, required);
    }
}
