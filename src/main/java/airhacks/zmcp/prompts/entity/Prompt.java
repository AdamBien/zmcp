package airhacks.zmcp.prompts.entity;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

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

    public Prompt fromJson(String json) {
        var jsonObject = new JSONObject(json);
        var name = jsonObject.getString("name");
        var description = jsonObject.getString("description");
        var arguments = jsonObject.getJSONArray("arguments");
        var argumentsList = arguments
                .toList()
                .stream()
                .map(PromptArgument::fromJson)
                .toList();
        return new Prompt(name, description, argumentsList);
    }
}
