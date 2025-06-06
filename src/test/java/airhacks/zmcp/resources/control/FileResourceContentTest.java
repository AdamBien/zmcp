package airhacks.zmcp.resources.control;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


public class FileResourceContentTest {

    @Test
    void isText() {
        var fileResourceContent = new FileAccess.FileResourceContent("text/plain", "Hello, world!");
        assertThat(fileResourceContent.isBlob()).isFalse();
        assertThat(fileResourceContent.encodedContent()).isEqualTo("\"Hello, world!\"");
    }

    @Test
    void isBlob() {
        var fileResourceContent = new FileAccess.FileResourceContent("app/binary", "Hello, world!");
        assertThat(fileResourceContent.isBlob()).isTrue();
        assertThat(fileResourceContent.encodedContent()).isEqualTo("SGVsbG8sIHdvcmxkIQ==");
    }
}
