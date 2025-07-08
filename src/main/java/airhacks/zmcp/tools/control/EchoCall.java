package airhacks.zmcp.tools.control;

import java.util.Map;
import java.util.function.Function;

import airhacks.zmcp.log.boundary.Log;

public class EchoCall implements Function<Map<String,Object>, Map<String, String>> {

    static Map<String,String> TOOL_SPEC = Map.of("name", "echo",
                                       "description", "Echo the input, useful for testing",
                                       "inputSchema","""
                                                {
            "type": "object",
            "properties": {
                "input": {
                    "type": "string"
                }
            },
            "required": ["input"]
        }
                                               """);

    public Map<String, String> apply(Map<String,Object> parameters) {
        Log.info("echoing: " + parameters);
        return Map.of("content", parameters.get("input").toString(),
                "error", "false");
    }
}
