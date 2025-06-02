package airhacks.zmcp.control;

import java.util.List;

import airhacks.zmcp.entity.Resource;

public interface ResourceAcces {
    
    static List<Resource> listResources() {
        return List.of(new Resource("file:/README.md", "Example Resource", "text/plain"));
    }
} 