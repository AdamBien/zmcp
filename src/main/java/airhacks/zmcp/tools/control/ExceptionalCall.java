package airhacks.zmcp.tools.control;

import airhacks.zmcp.tools.api.ToolExecutionResult;
import airhacks.zmcp.tools.api.ToolInvocation;
import airhacks.zmcp.tools.api.ToolSpec;

public class ExceptionalCall implements ToolInvocation{

    @ToolSpec(name = "error", description = "Throws an exception for testing")
    public ToolExecutionResult use(String parameters) {
        return ToolExecutionResult.error("This is an test execution error");
    }

    
}
