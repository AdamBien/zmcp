package airhacks.zmcp.resources.entity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import airhacks.zmcp.log.boundary.Log;

public record Resource(
        String uri,
        String name,
        String mimeType) {


    public static Resource fromPath(Path path) {
        var uri = path.toUri().toString();
        var name = path.getFileName().toString();
        var mimeType = "text/plain";
        try {
            mimeType = Files.probeContentType(path);
        } catch (IOException e) {
            Log.error("Error probing mime type: " + e);
        }
        return new Resource(uri, name, mimeType);
    }
    public String toJson() {
        return """
                {
                    "uri": "%s",
                    "name": "%s",
                    "mimeType": "%s"
                }""".formatted(uri(), name(), mimeType());
    }
}