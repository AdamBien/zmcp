package airhacks.zmcp.base.boundary;

import org.json.JSONException;
import org.json.JSONObject;

import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.resources.control.MessageSender;
import airhacks.zmcp.resources.entity.ResourceResponses;
import airhacks.zmcp.router.boundary.RequestHandler;
import airhacks.zmcp.router.entity.CoreMethods;
import airhacks.zmcp.router.entity.MCPRequest;

public class CoreSTDIOProtocol implements RequestHandler {

    boolean isInitialized = false;

    MessageSender messageSender;

    String capabilities;

    public CoreSTDIOProtocol(String capabilities) {
        this.messageSender = new MessageSender();
        this.capabilities = capabilities;
    }

    public boolean handleRequest(MCPRequest request) {
        var method = request.method();
        var id = request.id();
        var json = request.json();

        if (!isInitialized && !CoreMethods.INITIALIZE.isMethod(method)) {
            Log.error("Server not initialized, rejecting method: " + method);
            messageSender.sendError(id, -32002, "Server not initialized");
            return true;
        }

        var coreMethodOptional = CoreMethods.fromString(method);
        if (coreMethodOptional.isEmpty()) {
            Log.info("CoreSTDIOProtocol is not responsible for method: " + method);
            return false;
        }
        var coreMethod = coreMethodOptional.get();
        switch (coreMethod) {
            case INITIALIZE -> handleInitialize(id, json);
            case INITIALIZED -> handleInitialized();
        }
        return true;
    }

    void handleInitialize(Integer id, JSONObject json) {
        Log.info("Handling initialize request: " + json);
        try {
            var params = json.getJSONObject("params");
            var protocolVersion = params.getString("protocolVersion");
            var clientInfo = params.getJSONObject("clientInfo");
            var clientName = clientInfo.getString("name");
            var clientVersion = clientInfo.getString("version");

            Log.info("Initializing with protocol version: %s, client: %s %s".formatted(
                    protocolVersion, clientName, clientVersion));

            if (!"2025-03-26".equals(protocolVersion)) {
                Log.error("Unsupported protocol version: " + protocolVersion);
            }

            var initializeResponse = ResourceResponses.initialize(id, protocolVersion, this.capabilities);
            messageSender.send(initializeResponse);
            this.isInitialized = true;

        } catch (JSONException e) {
            Log.error("Error parsing initialize request: " + e.getMessage());
            messageSender.sendError(id, -32602, "Invalid params: " + e.getMessage());
        }
    }

    void handleInitialized() {
        Log.info("Client sent initialized notification");

    }
}
