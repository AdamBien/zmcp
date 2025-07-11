package airhacks.zmcp.tools.boundary;


import java.util.Map;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;

import airhacks.zmcp.base.control.MessageSender;
import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.router.boundary.RequestHandler;
import airhacks.zmcp.router.entity.Capability;
import airhacks.zmcp.router.entity.MCPRequest;
import airhacks.zmcp.tools.control.ToolExecutionResult;
import airhacks.zmcp.tools.control.ToolLocator;
import airhacks.zmcp.tools.entity.ToolsMethods;
import airhacks.zmcp.tools.entity.ToolsResponses;
import airhacks.zmcp.tools.entity.ToolsResposeContent;

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

    /*
     * https://modelcontextprotocol.io/specification/2025-03-26/server/tools#calling-tools
     */
    private void handleInvokeTool(int id, JSONObject json) {
        var params = json.getJSONObject("params");
        var toolName = params.getString("name");
        var arguments = toolArguments(params);
        var result = executeTool(id, toolName, arguments);
        if (result.isEmpty()) {
            return;
        }
        var callResult = result.get();
        var content = callResult.content();
        var responseContent = callResult.error() ? ToolsResposeContent.error(content) : ToolsResposeContent.text(content);
        var response =  ToolsResponses.toolCallTextContent(id, responseContent);
        messageSender.send(response);
    }

    static Map<String,Object> toolArguments(JSONObject params){
        var arguments = params.getJSONObject("arguments");
        return arguments.toMap();
    }

    Optional<ToolExecutionResult> executeTool(int id,String toolName, Map<String,Object> arguments) {
        var toolInstance = ToolLocator.findTool(toolName);
        if (toolInstance.isEmpty()) {
            Log.error("Tool not found: " + toolName);
            messageSender.sendInvalidRequest(id, "Tool not found: " + toolName);
            return Optional.empty();
        }
        Log.info("tool found: " + toolName);
        var result = toolInstance.get().use(arguments);
        if (result.emptyContent()) {
            Log.error("Error calling tool: " + toolName);
            messageSender.sendInvalidRequest(id, "Error calling tool: " + toolName);
            return Optional.empty();
        }
        Log.info("tools successfully called: " + toolName);
        return Optional.of(result);
    }

    void handleListTools(int id) {
        var tools = ToolLocator.all();
        Log.info("tools found: " + tools.size());
        var response = ToolsResponses.listTools(id, tools);
        messageSender.send(response);
    }

    @Override
    public Optional<Capability> capability() {
        return Optional.of(Capability.of("tools", true));
    }

}
