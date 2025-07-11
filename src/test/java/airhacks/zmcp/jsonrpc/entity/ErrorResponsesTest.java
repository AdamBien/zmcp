package airhacks.zmcp.jsonrpc.entity;

import org.junit.jupiter.api.Test;

import airhacks.zmcp.JSONAssertions;


public class ErrorResponsesTest {

    @Test
    void error() {

        var error = ErrorResponses.error(1, -32602, "Invalid params");
        var expected = """
                {
                    "jsonrpc": "2.0",
                    "id": 1,
                    "error": {
                        "code": -32602,
                        "message": "Invalid params"
                    }
                }""";
        JSONAssertions.assertEquals(error.toString(), expected);

    }
}
