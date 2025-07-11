package airhacks.zmcp.tools.entity;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import airhacks.zmcp.JSONAssertions;
import airhacks.zmcp.tools.control.ToolInstance;

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
        JSONAssertions.assertEquals(actual.toString(), expected);

    }

    @Test
    void listTools() {
        var echoTool = new ToolInstance(
            (input) -> Map.of("output", input.get("input").toString()),
            "echo",
            "Echo the input, useful for testing",
            ToolSpec.defaultInputSchema()
        );
        
        var errorTool = new ToolInstance(
            (input) -> Map.of("error", "Test error"),
            "error",
            "Throws an exception for testing",
            ToolSpec.defaultInputSchema()
        );
        

        var tools = List.of(echoTool, errorTool);
        
        var actual = ToolsResponses.listTools(1, tools);
        
        var expected = """
                {
                    "jsonrpc": "2.0",
                    "id": 1,
                    "result": {
                        "tools": [
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
                                    "required": ["input"]
                                }
                            },
                            {
                                "name": "error",
                                "description": "Throws an exception for testing",
                                "inputSchema": {
                                    "type": "object",
                                    "properties": {
                                        "input": {
                                            "type": "string"
                                        }
                                    },
                                    "required": ["input"]
                                }
                            }
                            
                        ]
                    }
                }""";
        
        JSONAssertions.assertEquals(actual.toString(), expected);
    }

    @Test
    void listToolsEmptyList() {
        var tools = List.<ToolInstance>of();
        var actual = ToolsResponses.listTools(42, tools);
        
        var expected = """
                {
                    "jsonrpc": "2.0",
                    "id": 42,
                    "result": {
                        "tools": []
                    }
                }""";
        
        JSONAssertions.assertEquals(actual.toString(), expected);
    }

    @Test
    void listToolsSingleTool() {
        var singleTool = new ToolInstance(
            (input) -> Map.of("result", "single tool result"),
            "singleTool",
            "A single tool for testing",
            ToolSpec.defaultInputSchema()
        );
        
        var tools = List.of(singleTool);
        var actual = ToolsResponses.listTools(123, tools);
        
        var expected = """
                {
                    "jsonrpc": "2.0",
                    "id": 123,
                    "result": {
                        "tools": [
                            {
                                "name": "singleTool",
                                "description": "A single tool for testing",
                                "inputSchema": {
                                    "type": "object",
                                    "properties": {
                                        "input": {
                                            "type": "string"
                                        }
                                    },
                                    "required": ["input"]
                                }
                            }
                        ]
                    }
                }""";
        
        JSONAssertions.assertEquals(actual.toString(), expected);
    }
}
