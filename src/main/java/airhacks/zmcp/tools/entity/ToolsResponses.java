package airhacks.zmcp.tools.entity;

import java.util.List;
import java.util.stream.Collectors;

import airhacks.zmcp.tools.control.ToolInstance;

public interface ToolsResponses {

    static String listTools(int id, List<ToolInstance> tools) {
        var toolJson = tools.stream()
                .map(ToolInstance::toJson)
                .collect(Collectors.joining(","));
        return """
                    {
                    "jsonrpc": "2.0",
                    "id": %d,
                    "result": {
                        "tools": [
                        %s
                        ]
                    }
                }
                 """.formatted(id, toolJson);
    }
}
