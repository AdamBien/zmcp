package airhacks.zmcp.resources.boundary;

import java.nio.file.Path;
import java.util.Optional;

import org.json.JSONObject;

import airhacks.zmcp.base.control.MessageSender;
import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.resources.control.FileAccess;
import airhacks.zmcp.resources.control.ResourceLister;
import airhacks.zmcp.resources.entity.ResourceResponses;
import airhacks.zmcp.resources.entity.ResourcesMethods;
import airhacks.zmcp.router.boundary.RequestHandler;
import airhacks.zmcp.router.entity.Capability;
import airhacks.zmcp.router.entity.MCPRequest;

/**
 * https://modelcontextprotocol.io/specification/2025-03-26/basic/lifecycle
 */
public class ResourcesSTDIOProtocol implements RequestHandler {

    MessageSender messageSender;
    FileAccess fileAccess;
    
    public ResourcesSTDIOProtocol(Path rootFolder) {
        this.fileAccess = FileAccess.of(rootFolder);
        this.messageSender = new MessageSender();
    }


    public boolean handleRequest(MCPRequest request) {
        Log.info("Processing request: " + request);
        var method = request.method();
        var id = request.id();
        var json = request.json();

        try {

            var optionalProtocol = ResourcesMethods.fromString(method);
            if (optionalProtocol.isEmpty()) {
                Log.debug("Method not found: " + method);
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
        } catch (IllegalStateException e) {
            Log.error("Error parsing JSON request: " + e);
            messageSender.sendError(id, -32700, "Invalid JSON-RPC request format");
        } catch (Exception e) {
            Log.error("Error handling request: " + e);
            messageSender.sendError(id, -32603, "Internal error: " + e.getMessage());
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
        var resourcesJson = ResourceLister.listResourcesAsJson(this.fileAccess);

        Log.info("Sending list resources response");
        var response = ResourceResponses.listResources(id, resourcesJson);
        messageSender.send(response);
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
        var fileResource = this.fileAccess.readFile(uri);
        var response = ResourceResponses.readResource(id, uri, fileResource);
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

    public Optional<Capability> capability(){
        return Optional.of(Capability.of("resources", true));
    }


}