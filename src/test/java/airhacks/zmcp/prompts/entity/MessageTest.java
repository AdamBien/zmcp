package airhacks.zmcp.prompts.entity;

import org.junit.jupiter.api.Test;

import airhacks.zmcp.JSONLoader;
import airhacks.zmcp.JSONAssertions;

public class MessageTest {
    @Test
    void toJson() {
        var message = new Message("user", "text", "Hello, world!");
        var json = message.toJson();
        var expected = JSONLoader.load("prompts", "hello_world_message");
        JSONAssertions.assertEquals(json, expected);
    }
}
