package airhacks.zmcp.prompts.entity;

import org.junit.jupiter.api.Test;

import airhacks.zmcp.JSONAssertions;

public class PromptArgumentTest {
    @Test
    void toJson() {
        var argument = new PromptArgument("code", "The code to review", true);
        var actual = argument.toJson();
        var expected = """
                {
                    "name": "code",
                    "description": "The code to review",
                    "required": true
                }
                """;
        JSONAssertions.assertEquals(expected, actual);
    }
}
