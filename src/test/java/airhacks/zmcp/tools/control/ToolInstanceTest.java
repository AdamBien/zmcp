package airhacks.zmcp.tools.control;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import airhacks.zmcp.JSONAssertions;
import airhacks.zmcp.tools.entity.ToolSpec;

public class ToolInstanceTest {

    @Test
    void creation() throws Exception {
        var tool = new EchoCall();
        var toolDescription = ToolInstance.of(tool);
        Assertions.assertThat(toolDescription).isPresent();
        var toolDescriptionValue = toolDescription.get();
        Assertions.assertThat(toolDescriptionValue.name()).isEqualTo("echo");
        Assertions.assertThat(toolDescriptionValue.description()).isEqualTo("Echo the input, useful for testing");
        var expectedInputSchema = ToolSpec.defaultInputSchema();
        JSONAssertions.assertEquals(expectedInputSchema,toolDescriptionValue.inputSchema());
    }

    @Test
    void toJsonWithInputSchema() {
        var tool = new EchoCall();
        var optionalInstance = ToolInstance.of(tool);
        assertThat(optionalInstance.isPresent()).isTrue();
        var toolInstance = optionalInstance.get();
        var actual = toolInstance.toJson();
        var expected = """
                {
                    "inputSchema": {
                        "type": "object",
                        "properties": {
                            "input": {
                                "type": "string"
                            }
                        },
                        "required": [
                            "input"
                        ]
                    },
                    "name": "echo",
                    "description": "Echo the input, useful for testing"
                }
                """;
        JSONAssertions.assertEquals(actual.toString(), expected);
    }
    
    @Test
    void defaulInputSchema() {
        var tool = new ExceptionalCall();
        var optionalInstance = ToolInstance.of(tool);
        assertThat(optionalInstance.isPresent()).isTrue();
        var toolInstance = optionalInstance.get();
        var actual = toolInstance.inputSchema();
        var expected = ToolSpec.defaultInputSchema();
        JSONAssertions.assertEquals(actual, expected);
    }

    @Test
    void fetchToolSpec() {
        var toolDescription = ToolInstance.fetchToolSpec(EchoCall.class);
        Assertions.assertThat(toolDescription).isPresent();
        var toolDescriptionValue = toolDescription.get();
        Assertions.assertThat(toolDescriptionValue.name()).isEqualTo("echo");
    }


    @Test
    void toJson() {
        var tool = new EchoCall();
        var optionalInstance = ToolInstance.of(tool);
        assertThat(optionalInstance.isPresent()).isTrue();
        var toolInstance = optionalInstance.get();
        var actual = toolInstance.toJson();
        var expected = """
                {
                    "name": "echo",
                    "description": "Echo the input, useful for testing",
                    "inputSchema": {
                        "type": "object",
                        "properties": {
                            "input": {
                                "type": "string"
                            }
                        },
                        "required": [
                            "input"
                        ]
                    }
                }
                """;
        JSONAssertions.assertEquals(actual.toString(), expected);
    }
}

