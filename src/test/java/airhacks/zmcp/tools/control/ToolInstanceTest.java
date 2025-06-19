package airhacks.zmcp.tools.control;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import airhacks.zmcp.tools.api.ToolSpec;

public class ToolInstanceTest {

    @Test
    void creation() throws Exception {
        var tool = new EchoCall();
        var toolDescription = ToolInstance.of(tool);
        Assertions.assertThat(toolDescription).isPresent();
        var toolDescriptionValue = toolDescription.get();
        Assertions.assertThat(toolDescriptionValue.name()).isEqualTo("echo");
        Assertions.assertThat(toolDescriptionValue.description()).isEqualTo("Echo the input, useful for testing");
        var expectedInputSchema = ToolSpec.class
                .getDeclaredMethod("inputSchema")
                .getDefaultValue();
        Assertions.assertThat(toolDescriptionValue.inputSchema()).isEqualTo(expectedInputSchema);
    }
}
