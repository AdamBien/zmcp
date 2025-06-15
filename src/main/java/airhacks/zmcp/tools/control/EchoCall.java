package airhacks.zmcp.tools.control;

import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.tools.api.ToolExecutionResult;
import airhacks.zmcp.tools.api.ToolInvocation;
import airhacks.zmcp.tools.api.ToolSpec;

public class EchoCall implements ToolInvocation {
    
    @ToolSpec(name = "echo", description = "Echo the input, useful for testing")
    public ToolExecutionResult use(String parameters) {
        Log.info("echoing: " + parameters);
        return ToolExecutionResult.success("Echo: " + parameters);
    }
}
