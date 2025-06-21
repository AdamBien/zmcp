package airhacks.zmcp.jsonrpc.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonRPCResponsesTest {

    @Test
    public void header() {
        var actual = JsonRPCResponses.header(1);
        var expected = """
                    "jsonrpc": "2.0",
                    "id": 1                    
                """;

        assertThat(actual).isEqualTo(expected);
    }
}
