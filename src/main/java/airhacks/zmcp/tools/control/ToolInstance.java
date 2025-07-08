package airhacks.zmcp.tools.control;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.json.JSONObject;

import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.tools.entity.ToolSpec;

/**
 * A ToolInstance represents a LLM tool and is used to execute the tool.
 * It is created from a tool function that takes an input and returns a result.
 * 
 * Concrete tool implementations have to implement: 
 * the {@link java.util.function.Function} with the following types <code>Function<Map<String,Object>,Map<String,String>></code>
 */
public record ToolInstance(Function<Map<String,Object>,Map<String,String>> tool, String name, String description, String inputSchema) {

    public ToolInstance{
        inputSchema = inputSchema == null || inputSchema.isBlank() ? ToolSpec.defaultInputSchema() : inputSchema;
    }
    
    public static Optional<ToolInstance> of(Function<Map<String,Object>,Map<String,String>> tool) {
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

    public ToolExecutionResult use(Map<String,Object> input) {
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
