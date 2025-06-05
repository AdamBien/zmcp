package airhacks.zmcp.resources.control;

import java.util.List;

import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.resources.entity.Resource;

public record FileAccess(String rootFolder) {
    
    public List<Resource> listResources() {
        return List.of(new Resource("file://README.md", "Example Resource", "text/plain"));
    }

    public String readResource(String path) {
        Log.info("Reading resource: " + path);
        return "Hello, World!";
    }
} 