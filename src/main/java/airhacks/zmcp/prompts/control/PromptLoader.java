package airhacks.zmcp.prompts.control;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.prompts.entity.Prompt;
import airhacks.zmcp.prompts.entity.PromptArgument;

public record PromptLoader(Path promptsDir) {

    public PromptLoader(String promptsDir) {
        this(Path.of(promptsDir));
    }

    public List<Prompt> all() {
        return List.of(
                new Prompt("code_review", "Asks the LLM to analyze code quality and suggest improvements",
                        List.of(new PromptArgument("code", "The code to review", true))));

    }

    public Optional<Prompt> get(String name) {
        Log.info("getting prompt: " + name);
        return Optional.of(all().getFirst());
    }

}
