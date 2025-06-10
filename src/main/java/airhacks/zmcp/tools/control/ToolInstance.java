package airhacks.zmcp.tools.control;

import java.lang.reflect.Method;
import java.util.Optional;

import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.tools.api.ToolInvocation;
import airhacks.zmcp.tools.api.ToolSpec;

public record ToolInstance(ToolInvocation tool, String name, String description, String inputSchema) {
    
    public static Optional<ToolInstance> of(ToolInvocation tool) {
        var toolClass = tool.getClass();
        Method toolMethod;
        try {
            toolMethod = toolClass.getMethod("use", String.class);
        } catch (NoSuchMethodException e) {
            Log.error("Tool " + toolClass.getName() + " does not have an invoke method");
            return Optional.empty();
        }

        var toolSpec = toolMethod.getAnnotation(ToolSpec.class);
        
        var name = toolSpec.name();
        var description = toolSpec.description();
        var inputSchema = toolSpec.inputSchema();
        var toolDescription = new ToolInstance(tool, name, description, inputSchema);
        return Optional.of(toolDescription);
    }

    public Optional<String> use(String input) {
        return tool.use(input);
    }


    public String toJson() {
        return """
            {
                "name": "%s",
                "description": "%s",
                "inputSchema": %s
            }
        """.formatted(name, description, inputSchema);
    }

}
