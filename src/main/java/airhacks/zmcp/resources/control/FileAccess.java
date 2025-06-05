package airhacks.zmcp.resources.control;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.resources.entity.Resource;

public record FileAccess(Path rootFolder) {

    public static FileAccess of(String rootFolder){
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



    public String readResource(String path) {
        Log.info("Reading resource: " + path);
        return "Hello, World!";
    }
} 