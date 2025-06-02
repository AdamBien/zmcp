package airhacks.zmcp.boundary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

import airhacks.zmcp.control.ResourceAcces;
import airhacks.zmcp.entity.Resource;

public class StdioTransport {
    private final BufferedReader reader;
    private final PrintWriter writer;

    public StdioTransport() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.writer = new PrintWriter(System.out, true);
    }

    public void start() throws IOException {
        while (true) {
            var line = reader.readLine();
            if (line == null) {
                break;
            }
            handleRequest(line);
        }
    }

    private void handleRequest(String request) {
        try {
            switch (request) {
                case "resources/list" -> handleListResources();
                case "resources/read" -> handleReadResource();
                case "resources/subscribe" -> handleSubscribe();
                case "resources/unsubscribe" -> handleUnsubscribe();
                default -> writer.println("{\"error\": \"Unknown command: " + request + "\"}");
            }
        } catch (Exception e) {
            writer.println("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void handleListResources() {
        var resources = ResourceAcces.listResources();
        var response = formatResponse(resources);
        writer.println(response);
    }

    private void handleReadResource() {
        // TODO: Implement resource reading
        writer.println("{\"error\": \"Not implemented yet\"}");
    }

    private void handleSubscribe() {
        // TODO: Implement resource subscription
        writer.println("{\"error\": \"Not implemented yet\"}");
    }

    private void handleUnsubscribe() {
        // TODO: Implement resource unsubscription
        writer.println("{\"error\": \"Not implemented yet\"}");
    }

    private String formatResponse(List<Resource> resources) {
        var resourcesJson = resources.stream()
            .map(Resource::toJson)
            .collect(Collectors.joining(","));
            
        return String.format("{\"resources\": [%s]}", resourcesJson);
    }
} 