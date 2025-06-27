package airhacks.zmcp.prompts.entity;

import java.util.List;

import org.junit.jupiter.api.Test;

import airhacks.zmcp.JSONAssertions;
import airhacks.zmcp.JSONLoader;


public class PromptResponsesTest {

    @Test
    void listPrompts() {
        var prompts = List.of(
                new PromptAnnouncement("code_review", "Asks the LLM to analyze code quality and suggest improvements", List.of(
                        new PromptArgument("code", "The code to review", true))));
        var actual = PromptResponses.listPrompts(1, prompts);
        var expected = JSONLoader.load("promptresponses", "listing_prompts_response");
        JSONAssertions.assertEquals(actual, expected);
    }

    @Test
    void getPrompt() {
        var description = "Code review prompt";
        var actual = PromptResponses.getPrompt(1,  description, new Message("user", "text", "Hello, world!"));
        var expected = JSONLoader.loadPromptResponse("get_prompt_response");
        JSONAssertions.assertEquals(actual, expected);
    }
}
