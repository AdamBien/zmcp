package airhacks.zmcp.tools.boundary;

import org.json.JSONException;
import org.json.JSONObject;

import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.resources.control.MessageSender;
import airhacks.zmcp.router.boundary.RequestHandler;
import airhacks.zmcp.router.entity.MCPRequest;
import airhacks.zmcp.tools.control.ToolLocator;
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
        var method = request.method();
        var id = request.id();
        var json = request.json();

        try {

            var optionalProtocol = ToolsMethods.fromString(method);
            if (optionalProtocol.isEmpty()) {
                Log.info("ToolsSTDIOProtocol is not responsible for: " + method);
                return false;
            }
            var protocol = optionalProtocol.get();
            switch (protocol) {
                case TOOLS_LIST -> handleListTools(id);
                case TOOLS_CALL -> handleInvokeTool(id, json);
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

    private void handleInvokeTool(int id, JSONObject json) {
        var toolName = json.getString("tool");
        var params = json.getJSONObject("params");
        var toolInstance = ToolLocator.findTool(toolName);
        if (toolInstance.isEmpty()) {
            Log.error("Tool not found: " + toolName);
            messageSender.sendInvalidRequest(id, "Tool not found: " + toolName);
            return;
        }

        var result = toolInstance.get().use(params.toString());
        if (result.isEmpty()) {
            Log.error("Error calling tool: " + toolName);
            messageSender.sendInvalidRequest(id, "Error calling tool: " + toolName);
            return;
        }
        messageSender.send(result.get());
    }

    void handleListTools(int id) {
        Log.info("Sending list of tools");
        var response = ToolsResponses.listTools(id);
        messageSender.send(response);
    }

}
