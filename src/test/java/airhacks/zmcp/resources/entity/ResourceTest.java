package airhacks.zmcp.resources.entity;

import org.junit.jupiter.api.Test;

import airhacks.zmcp.JSONAssertions;

public class ResourceTest {

    @Test
    public void testToJson() {
        var resource = new Resource("file:///home/duke/Coffee.java", "Coffee.java", "text/plain");
        var actual = resource.toJson();
        var expected = """
                   {
                  "uri": "file:///home/duke/Coffee.java",
                  "name": "Coffee.java",
                  "mimeType": "text/plain"
                }
                  """;

        JSONAssertions.assertEquals(actual,
                expected);
    }
}
