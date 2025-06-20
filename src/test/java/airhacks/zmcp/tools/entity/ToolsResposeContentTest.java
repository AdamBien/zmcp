package airhacks.zmcp.tools.entity;

import org.junit.jupiter.api.Test;

import airhacks.zmcp.JSONAssertions;

public class ToolsResposeContentTest {

    @Test
    void toJson() {
        var content = ToolsResposeContent.text("Hello, world!");
        var json = content.toJson();
        var expected = """
                {
                 "type": "text",
                 "text": "Hello, world!"
                }""";
       JSONAssertions.assertEquals(json, expected);
    }
}
