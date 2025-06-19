package airhacks.zmcp.prompts.entity;

import airhacks.zmcp.JSONLoader;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PromptTest {
    
    @Test
    void fromJson() {
        var json = JSONLoader.load("prompts", "hello_world_prompt");
        var prompt = Prompt.fromJson(json);
        assertThat(prompt.description()).isEqualTo("Hello world prompt");
        assertThat(prompt.message().role()).isEqualTo("user");
        assertThat(prompt.message().type()).isEqualTo("text");
        assertThat(prompt.message().content()).isEqualTo("Hello, world!");   
    }
}
