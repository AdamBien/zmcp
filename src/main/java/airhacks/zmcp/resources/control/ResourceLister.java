package airhacks.zmcp.resources.control;

import java.util.List;

import org.json.JSONArray;

import airhacks.zmcp.resources.entity.Resource;

public interface ResourceLister {

    static String listResourcesAsJson(FileAccess fileAccess) {
        var resources = fileAccess.listResources();
        return toJsonArray(resources);
    }

    static String toJsonArray(List<Resource> resources) {
        var jsonArray = new JSONArray();
        resources.stream()
                .map(Resource::toJson)
                .forEach(jsonArray::put);
        
        return jsonArray.toString();
    }
}