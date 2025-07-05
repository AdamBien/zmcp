package airhacks.zmcp.prompts.entity;

import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import airhacks.zmcp.JSONAssertions;
import airhacks.zmcp.JSONLoader;


public class PromptResponsesTest {

    @Test
    void listPrompts() {
        var prompts = List.of(
                new PromptSignature("code_review", "Asks the LLM to analyze code quality and suggest improvements", List.of(
                        new PromptArgument("code", "The code to review", true))));
        var actual = PromptResponses.listPrompts(1, prompts);
        var expectedString = JSONLoader.load("promptresponses", "listing_prompts_response");
        var expected = new JSONObject(expectedString);
        JSONAssertions.assertEquals(actual, expected);
    }

    @Test
    void getPrompt() {
        var description = "Code review prompt";
        var actual = PromptResponses.getPrompt(1,  new PromptInstance(new PromptSignature("code_review", "Code review prompt", List.of(new PromptArgument("code", "The code to review", true))), description, new Message("user", "text", "Hello, world!")));
        var expectedString = JSONLoader.loadPromptResponse("get_prompt_response");
        var expected = new JSONObject(expectedString);
        JSONAssertions.assertEquals(actual, expected);
    }
}
