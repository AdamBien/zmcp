package airhacks.zmcp.resources.entity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.json.JSONObject;

import airhacks.zmcp.log.boundary.Log;
/**
 * represents a resource according to: https://modelcontextprotocol.io/docs/concepts/resources#resource-discovery
 */
public record Resource(
        String uri,
        String name,
        String title,
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
        var title = name;
        return new Resource(uri, name, title, mimeType);
    }
    public String toJson() {
        var json = new JSONObject();
        json.put("uri", uri());
        json.put("name", name());
        json.put("title", title());
        json.put("mimeType", mimeType());
        return json.toString();
    }
}