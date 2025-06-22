package airhacks.zmcp.prompts.entity;

import java.util.List;

import org.junit.jupiter.api.Test;

import airhacks.zmcp.JSONAssertions;

public class PromptAnnouncementTest {
    @Test
    void toJson() {
        var expected = """
                {
                    "name": "code_review",
                    "description": "Asks the LLM to analyze code quality and suggest improvements",
                    "arguments": [
                        {"name": "code", "description": "The code to review", "required": true}
                    ]
                }
                """;
        var announcement = new PromptAnnouncement("code_review", "Asks the LLM to analyze code quality and suggest improvements",
                List.of(new PromptArgument("code", "The code to review", true)));
        var actual = announcement.toJson();
        JSONAssertions.assertEquals(expected, actual);
    }
}
