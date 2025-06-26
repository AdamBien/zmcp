package airhacks.zmcp.prompts.control;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

public class PromptLoaderTest {
    @Test
    void allPrompts() {
        var promptsDir = Path.of("src/test/json/prompts");
        var loader = new PromptLoader(promptsDir);
        var prompts = loader.allPrompts();
        assertThat(prompts).isNotEmpty();   
    }
}
