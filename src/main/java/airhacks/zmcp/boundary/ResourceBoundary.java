package airhacks.zmcp.boundary;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import airhacks.zmcp.control.ResourceAcces;
import airhacks.zmcp.entity.Resource;

public class ResourceBoundary implements HttpHandler {
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, 0); // Method Not Allowed
            exchange.close();
            return;
        }
        
        var resources = ResourceAcces.listResources();
        var response = formatResponse(resources);
        
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.getBytes().length);
        
        try (var os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
    
    private String formatResponse(List<Resource> resources) {
        var resourcesJson = resources.stream()
            .map(Resource::toJson)
            .collect(Collectors.joining(","));
            
        return String.format("{\"resources\": [%s]}", resourcesJson);
    }
} 