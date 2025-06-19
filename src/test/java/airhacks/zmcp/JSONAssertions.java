package airhacks.zmcp;

import org.json.JSONObject;

import static org.assertj.core.api.Assertions.assertThat;

public interface JSONAssertions {
    
    static void assertEquals(String actual, String expected) {
        var actualJson = new JSONObject(actual);
        var expectedJson = new JSONObject(expected);
        assertThat(actualJson.toString()).isEqualTo(expectedJson.toString());
    }
}
