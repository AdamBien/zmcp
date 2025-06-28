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

    @Test
    void nameFromPath() {
        var path = Path.of("src/test/json/prompts/duke.json");
        var name = PromptLoader.nameFromPath(path);
        assertThat(name).isEqualTo("duke");
    }

    @Test
    void findPromptByName() {
        var promptsDir = Path.of("src/test/json/prompts");
        var loader = new PromptLoader(promptsDir);
        var prompt = loader.get("duke");
        assertThat(prompt).isNotNull();
        assertThat(prompt.get().signature().name()).isEqualTo("duke");
    }
}
