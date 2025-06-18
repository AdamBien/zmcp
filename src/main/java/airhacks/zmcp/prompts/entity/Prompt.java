package airhacks.zmcp.prompts.entity;

import java.util.List;
import java.util.stream.Collectors;

public record Prompt(String name, String description, List<PromptArgument> arguments) {
    
    public String toJson() {
        var argumentsJson = arguments.stream()
                .map(PromptArgument::toJson)
                .collect(Collectors.joining(","));
        return """
                {
                    "name": "%s",
                    "description": "%s",
                    "arguments": [
                        %s
                    ]
                }
                """.formatted(name, description, argumentsJson);
    }
}
