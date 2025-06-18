package airhacks.zmcp.prompts.entity;

import java.util.List;
import java.util.stream.Collectors;

import airhacks.zmcp.jsonrpc.entity.JsonRPCResponses;

public interface PromptResponses {

    static String listPrompts(int id, List<Prompt> prompts) {
        var promptJson = prompts
                .stream()
                .map(Prompt::toJson)
                .collect(Collectors.joining(","));
        var header = JsonRPCResponses.header(id);
        return """
                    {
                    %s,
                    "result": {
                        "prompts": [
                        %s
                        ]
                    }
                }
                """.formatted(header, promptJson);
    }

    static String getPrompt(int id, String description,Message message) {
        var header = JsonRPCResponses.header(id);
        return """
                    {
                    %s,
                    "result": {
                        "description": %s,
                        "messages": [
                            %s
                        ]
                    }
                    }
                    """.formatted(header, description, message.toJson());
    }
}
