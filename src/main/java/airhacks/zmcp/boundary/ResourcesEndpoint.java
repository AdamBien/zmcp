package airhacks.zmcp.boundary;

import airhacks.zmcp.control.ResourceAcces;
import airhacks.zmcp.entity.Resource;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

public class ResourcesEndpoint implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, 0); // Method Not Allowed
            exchange.close();
            return;
        }

        List<Resource> resources = ResourceAcces.listResources();
        String response = formatResponse(resources);

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private String formatResponse(List<Resource> resources) {
        String resourcesJson = resources
                .stream()
                .map(resource -> String.format(
                        """
                                {
                                    "uri": "%s",
                                    "name": "%s",
                                    "mimeType": "%s"
                                }""",
                        resource.uri(),
                        resource.name(),
                        resource.mimeType()))
                .collect(Collectors.joining(","));

        return String.format("{\"resources\": [%s]}", resourcesJson);
    }
}