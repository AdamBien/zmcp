package airhacks.zmcp.resources.control;

import java.util.List;

import airhacks.zmcp.resources.entity.Resource;

public interface ResourceLister {

    static String listResourcesAsJson(FileAccess fileAccess) {
        var resources = fileAccess.listResources();
        return toJsonArray(resources);
    }

    static String toJsonArray(List<Resource> resources) {
        var jsonArray = resources.stream()
                .map(Resource::toJson)
                .toList();
        
        return "[" + String.join(",", jsonArray) + "]";
    }
}