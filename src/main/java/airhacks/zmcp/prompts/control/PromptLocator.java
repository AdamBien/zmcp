package airhacks.zmcp.prompts.control;

import java.util.List;
import java.util.Optional;

import airhacks.zmcp.prompts.entity.Prompt;
import airhacks.zmcp.prompts.entity.PromptArgument;

public interface PromptLocator {

    static List<Prompt> all() {
        return List.of(
                new Prompt("code_review", "Asks the LLM to analyze code quality and suggest improvements",
                        List.of(new PromptArgument("code", "The code to review", true))));

    }

    static Optional<Prompt> get(String name) {
        return Optional.of(all().getFirst());
    }
}
