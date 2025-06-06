package airhacks.zmcp.resources.boundary;

import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;

import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.resources.control.MessageSender;
import airhacks.zmcp.resources.control.FileAccess;
import airhacks.zmcp.resources.entity.Resource;
import airhacks.zmcp.resources.entity.ResourceResponses;
import airhacks.zmcp.resources.entity.ResourcesMethods;
import airhacks.zmcp.router.boundary.RequestHandler;
import airhacks.zmcp.router.entity.MCPRequest;

/**
 * https://modelcontextprotocol.io/specification/2025-03-26/basic/lifecycle
 */
public class ResourcesSTDIOProtocol implements RequestHandler {

    MessageSender messageSender;
    FileAccess fileAccess;
    
    public ResourcesSTDIOProtocol(String rootFolder) {
        this.fileAccess = FileAccess.of(rootFolder);
        this.messageSender = new MessageSender();
    }


    public boolean handleRequest(MCPRequest request) {
        Log.info("Processing request: " + request);
        try {
            var method = request.method();
            var id = request.id();
            var json = request.json();

            var optionalProtocol = ResourcesMethods.fromString(method);
            if (optionalProtocol.isEmpty()) {
                Log.error("Method not found: " + method);
                return false;
            }
            var protocol = optionalProtocol.get();
            switch (protocol) {
                case RESOURCES_LIST -> handleListResources(id);
                case NOTIFICATIONS_INITIALIZED -> handleNotificationsInitialized();
                case NOTIFICATIONS_CANCELLED -> handleNotificationsCancelled();
                case RESOURCES_READ -> handleReadResource(id, json);
                case SUBSCRIBE -> handleSubscribe(id);
                case UNSUBSCRIBE -> handleUnsubscribe(id);
            }
        } catch (JSONException e) {
            Log.error("Error parsing JSON request: " + e);
            messageSender.sendError(null, -32700, "Invalid JSON-RPC request format");
        } catch (Exception e) {
            Log.error("Error handling request: " + e);
            messageSender.sendError(null, -32603, "Internal error: " + e.getMessage());
        }
        return true;
    }



    void handleNotificationsInitialized() {
        Log.info("Client sent notifications initialized notification");
    }

    void handleNotificationsCancelled() {
        Log.info("Client sent notifications cancelled notification");
    }

    void handleListResources(Integer id) {
        Log.info("Handling list resources request");
        var resources = this.fileAccess.listResources();
        var resourcesJson = resources.stream()
                .map(Resource::toJson)
                .collect(Collectors.joining(","));

        Log.info("Sending list resources response");
        messageSender.send(ResourceResponses.listResources(id, resourcesJson));
    }

    /**
     * https://modelcontextprotocol.io/specification/2025-03-26/server/resources#reading-resources
     * {
     * "jsonrpc": "2.0",
     * "id": 2,
     * "method": "resources/read",
     * "params": {
     * "uri": "file:///project/src/main.rs"
     * }
     * }
     * 
     * @param id
     * @param json
     */
    void handleReadResource(Integer id, JSONObject json) {
        Log.info("Handling read resource request");
        var params = json.getJSONObject("params");
        var uri = params.getString("uri");
        var content = this.fileAccess.readFile(uri);
        var response = ResourceResponses.readResource(id, uri, "text/plain", content);
        messageSender.send(response);
    }


    void handleSubscribe(Integer id) {
        Log.info("Handling subscribe request");
        messageSender.methodNotImplementedYet(id);
    }

    void handleUnsubscribe(Integer id) {
        Log.info("Handling unsubscribe request");
        messageSender.methodNotImplementedYet(id);
    }



}