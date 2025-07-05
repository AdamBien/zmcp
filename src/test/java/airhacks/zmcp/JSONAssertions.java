package airhacks.zmcp;

import static org.assertj.core.api.Assertions.assertThat;

import org.json.JSONObject;

public interface JSONAssertions {
    
    static void assertEquals(String actual, String expected) {
        var actualJson = new JSONObject(actual);
        var expectedJson = new JSONObject(expected);
        assertThat(actualJson.toString()).isEqualTo(expectedJson.toString());
    }

    static void assertEquals(String actual, JSONObject expected) {
        var actualJson = new JSONObject(actual);
        assertThat(actualJson.toString()).isEqualTo(expected.toString());
    }

    static void assertEquals(JSONObject actual, JSONObject expected) {
        assertThat(actual.toString()).isEqualTo(expected.toString());
    }
}
