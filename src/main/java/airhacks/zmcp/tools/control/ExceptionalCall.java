package airhacks.zmcp.tools.control;

import java.util.Map;
import java.util.function.Function;

public class ExceptionalCall implements Function<Map<String,Object>, Map<String, String>> {

    static Map<String, String> TOOL_SPEC = Map.of("name", "error",
            "description", "Throws an exception for testing");

    public Map<String, String> apply(Map<String,Object> parameters) {
        return Map.of("error", "true",
                "content", "This is an test execution error");
    }

}
