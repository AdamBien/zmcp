package airhacks.zmcp.resources.control;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;

import org.json.JSONObject;

import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.resources.entity.Resource;

public record FileAccess(Path rootFolder) {

    static List<String> TEXT_MIME_TYPES = List.of("text/plain", "text/markdown", "text/html", "text/css",
            "text/javascript", "application/json", "application/xml");
    static String UNKNOWN_MIME_TYPE = "application/octet-stream";

    public static FileAccess of(String rootFolder) {
        return new FileAccess(Path.of(rootFolder));
    }

    public List<Resource> listResources() {
        try {
            return Files.walk(rootFolder)
                    .filter(Files::isRegularFile)
                    .map(path -> rootFolder.relativize(path))
                    .map(Resource::fromPath)
                    .toList();
        } catch (IOException e) {
            Log.error("Error listing resources: " + e);
            return List.of();
        }
    }

    public record FileResourceContent(String mimeType, String content) {

        public boolean isBlob() {
            return !TEXT_MIME_TYPES.contains(this.mimeType.toLowerCase());
        }

        public String encodedContent() {
            if (!this.isBlob()) {
                return JSONObject.quote(this.content);
            }
            var bytes = this.content.getBytes();
            return Base64.getEncoder()
                    .encodeToString(bytes);
        }
    }

    public FileResourceContent readFile(String uriString) {
        Log.info("Reading resource: " + uriString);
        var uri = URI.create(uriString);
        var path = uri.getPath();
        try {
            var resolvedPath = rootFolder.resolve(path);
            var content = Files.readString(resolvedPath);
            var mimeType = Files.probeContentType(resolvedPath);
            if (mimeType == null) {
                mimeType = UNKNOWN_MIME_TYPE;
            }
            return new FileResourceContent(mimeType, content);
        } catch (IOException e) {
            Log.error("Error reading resource: " + e);
            return new FileResourceContent(null, null);
        }
    }
}