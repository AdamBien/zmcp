package airhacks.zmcp.tools.control;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ToolInstanceTest {
    @Test
    void testOf() {
        var tool = new EchoCall();
        var toolDescription = ToolInstance.of(tool);
        Assertions.assertThat(toolDescription).isPresent();
        Assertions.assertThat(toolDescription.get().name()).isEqualTo("echo");
        Assertions.assertThat(toolDescription.get().description()).isEqualTo("Echo the input, useful for testing");
        Assertions.assertThat(toolDescription.get().inputSchema()).isEqualTo("string");
    }
}
