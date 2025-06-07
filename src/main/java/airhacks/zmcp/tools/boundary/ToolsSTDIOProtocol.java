package airhacks.zmcp.tools.boundary;

import org.json.JSONException;

import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.resources.control.MessageSender;
import airhacks.zmcp.router.boundary.RequestHandler;
import airhacks.zmcp.router.entity.MCPRequest;
import airhacks.zmcp.tools.entity.ToolsMethods;
import airhacks.zmcp.tools.entity.ToolsResponses;

/**
 * https://modelcontextprotocol.io/specification/2025-03-26/server/tools#user-interaction-model
 */
public class ToolsSTDIOProtocol implements RequestHandler {

    MessageSender messageSender;

    public ToolsSTDIOProtocol() {
        this.messageSender = new MessageSender();
    }

    @Override
    public boolean handleRequest(MCPRequest request) {
        Log.info("Processing request: " + request);
        try {
            var method = request.method();
            var id = request.id();
            var json = request.json();

            var optionalProtocol = ToolsMethods.fromString(method);
            if (optionalProtocol.isEmpty()) {
                Log.info("ToolsSTDIOProtocol is not responsible for: " + method);
                return false;
            }
            var protocol = optionalProtocol.get();
            switch (protocol) {
                case TOOLS_LIST -> handleListTools(id);
            }
        } catch (JSONException e) {
            Log.error("Error parsing JSON request: " + e);
            messageSender.sendInvalidJSONRPCRequestFormat(id);
        } catch (Exception e) {
            Log.error("Error handling request: " + e);
            messageSender.sendInternalError(id, e.getMessage());
        }
        return true;

    }

    void handleListTools(int id) {
    }

}
