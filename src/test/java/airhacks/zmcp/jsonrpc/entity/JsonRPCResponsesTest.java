package airhacks.zmcp.jsonrpc.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonRPCResponsesTest {

    @Test
    public void responses() {
        var actual = JsonRPCResponses.response(1);
        assertThat(actual.get("jsonrpc")).isEqualTo("2.0");
        assertThat(actual.get("id")).isEqualTo(1);
    }
}
