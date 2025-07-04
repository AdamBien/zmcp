package airhacks.zmcp.tools.control;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class ToolLocatorTest {
    
    @Test
    void all() {
    var tools = ToolLocator.all();    
    assertThat(tools).isNotEmpty();
    assertThat(tools).hasSize(2);
    }
}
