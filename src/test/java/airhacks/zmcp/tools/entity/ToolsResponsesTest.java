package airhacks.zmcp.tools.entity;

import org.junit.jupiter.api.Test;

import airhacks.zmcp.JSONAssertions;

public class ToolsResponsesTest {
    @Test
    void toolCallTextContent() {
        var toolResponsesContent = ToolsResposeContent.text("hey, duke");
        var actual = ToolsResponses.toolCallTextContent(1, toolResponsesContent);
        var expected = """
                {
                    "result": {
                        "isError": false,
                        "content": [
                            {
                                "text": "hey, duke",
                                "type": "text"
                            }
                        ]
                    },
                    "id": 1,
                    "jsonrpc": "2.0"
                }
                    """;
        JSONAssertions.assertEquals(actual, expected);

    }
}
