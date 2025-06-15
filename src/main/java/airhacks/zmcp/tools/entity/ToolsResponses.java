package airhacks.zmcp.tools.entity;

import java.util.List;
import java.util.stream.Collectors;

import airhacks.zmcp.jsonrpc.entity.JsonRPCResponses;
import airhacks.zmcp.tools.control.ToolInstance;

public interface ToolsResponses {

    static String listTools(int id, List<ToolInstance> tools) {
        var toolJson = tools.stream()
                .map(ToolInstance::toJson)
                .collect(Collectors.joining(","));
        var header = JsonRPCResponses.header(id);
        return """
                    {
                    %s,
                    "result": {
                        "tools": [
                        %s
                        ]
                    }
                }
                 """.formatted(header, toolJson);
    }

    /**
     * https://modelcontextprotocol.io/specification/2025-03-26/server/tools#calling-tools
     */
    static String toolCallTextContent(int id, ToolsResposeContent response) {
        var header = JsonRPCResponses.header(id);
        return """
                {
                    %s,
                    "result": {
                        "content": [%s],
                        "isError": %s
                    }
                }
                """.formatted(header, response.toJson(), response.error());
    }
}
