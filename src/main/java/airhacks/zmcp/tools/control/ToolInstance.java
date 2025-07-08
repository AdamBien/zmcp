package airhacks.zmcp.tools.control;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.json.JSONObject;

import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.tools.entity.ToolSpec;


public record ToolInstance(Function<String,Map<String,String>> tool, String name, String description, String inputSchema) {

    public ToolInstance{
        inputSchema = inputSchema == null || inputSchema.isBlank() ? ToolSpec.defaultInputSchema() : inputSchema;
    }
    
    public static Optional<ToolInstance> of(Function<String,Map<String,String>> tool) {
        var toolClass = tool.getClass();
        var toolSpecResult = fetchToolSpec(toolClass);
        if (toolSpecResult.isEmpty()) {
            Log.error("Tool " + toolClass.getName() + " does not have a tool spec");
            return Optional.empty();
        }
        var toolSpec = toolSpecResult.get();
        var name = toolSpec.name();
        var description = toolSpec.description();
        var inputSchema = toolSpec.inputSchema();
        var toolDescription = new ToolInstance(tool, name, description, inputSchema);
        return Optional.of(toolDescription);
    }

    public ToolExecutionResult use(String input) {
        var result = tool.apply(input);
        return ToolExecutionResult.of(result);
    }

    public boolean hasName(String name) {
        return this.name.equals(name);
    }

    static Optional<ToolSpec> fetchToolSpec(Class<?> toolClass) {
        try {
            var toolSpecField = toolClass.getDeclaredField("TOOL_SPEC");
            toolSpecField.setAccessible(true);
            var toolSpec = toolSpecField.get(null);
            return switch (toolSpec) {
                case Map toolSpecMap -> Optional.of(ToolSpec.of(toolSpecMap));
                default -> Optional.empty();
            };
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            Log.error("Tool " + toolClass.getName() + " does not have a tool spec");
            return Optional.empty();
        }
    }
    public JSONObject toJson() {
        var jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("description", description);
        jsonObject.put("inputSchema", new JSONObject(inputSchema));
        return jsonObject;
    }

}
