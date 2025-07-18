package airhacks.zmcp.tools.entity;

import java.util.Map;

public record ToolSpec(String name, String description, String inputSchema) {

    public static ToolSpec of(String name, String description, String inputSchema) {
        return new ToolSpec(name, description, inputSchema);
    }

    public static ToolSpec of(Map<String, String> toolSpec) {
        var name = toolSpec.get("name");    
        var description = toolSpec.get("description");
        var inputSchema = toolSpec.getOrDefault("inputSchema",defaultInputSchema());
        if (inputSchema == null) {
            inputSchema = defaultInputSchema();
        }
        return new ToolSpec(name, description, inputSchema);
    }

    public static String defaultInputSchema() {
        return """
                {
                    "type": "object",
                    "properties": {
                        "input": {
                            "type": "string"
                        }
                    },
                    "required": ["input"]
                }
                """;
    }
}
