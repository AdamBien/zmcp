package airhacks.zmcp.prompts.entity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PromptResponsesTest {

    @Test
    void listPrompts() throws IOException {
        var prompts = List.of(
                new Prompt("code_review", "Asks the LLM to analyze code quality and suggest improvements", List.of(
                        new PromptArgument("code", "The code to review", true))));
        var response = PromptResponses.listPrompts(1, prompts);
        var expected = Files.readString(Path.of("src/test/json/prompts/listing_prompts_response.json"));
        assertThat(response).isEqualTo(expected);
    }
}
