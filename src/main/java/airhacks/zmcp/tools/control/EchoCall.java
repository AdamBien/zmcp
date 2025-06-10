package airhacks.zmcp.tools.control;

import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.tools.api.ToolInvocation;
import airhacks.zmcp.tools.api.ToolSpec;

import java.util.Optional;

public class EchoCall implements ToolInvocation {
    
    @ToolSpec(name = "echo", description = "Echo the input, useful for testing", inputSchema = "string")
    public Optional<String> use(String parameters) {
        Log.info("echoing: " + parameters);
        return Optional.of("Echo: " + parameters);
    }
}
