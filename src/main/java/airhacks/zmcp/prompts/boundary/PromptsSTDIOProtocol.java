package airhacks.zmcp.prompts.boundary;

import java.nio.file.Path;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;

import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.prompts.control.PromptLoader;
import airhacks.zmcp.prompts.entity.PromptResponses;
import airhacks.zmcp.prompts.entity.PromptsMethods;
import airhacks.zmcp.resources.control.MessageSender;
import airhacks.zmcp.router.boundary.RequestHandler;
import airhacks.zmcp.router.entity.Capability;
import airhacks.zmcp.router.entity.MCPRequest;

public class PromptsSTDIOProtocol implements RequestHandler {

    MessageSender messageSender;
    PromptLoader promptLoader;

    public PromptsSTDIOProtocol(Path promptsDir) {
        this.messageSender = new MessageSender();
        this.promptLoader = new PromptLoader(promptsDir);
    }

    @Override
    public Optional<Capability> capability() {
        return Optional.of(Capability.of("prompts", true));
    }

    /**
     * https://modelcontextprotocol.io/specification/2025-03-26/server/prompts#protocol-messages
     */
    @Override
    public boolean handleRequest(MCPRequest request) {
        Log.info("Processing request: " + request);
        var method = request.method();
        var id = request.id();
        var json = request.json();

        try {

            var optionalProtocol = PromptsMethods.fromString(method);
            if (optionalProtocol.isEmpty()) {
                Log.info("PromptsSTDIOProtocol is not responsible for: " + method);
                return false;
            }
            var protocol = optionalProtocol.get();
            switch (protocol) {
                case PROMPTS_LIST -> handleListPrompts(id);
                case PROMPTS_GET -> handleGetPrompt(id, json);
            }
        } catch (JSONException e) {
            Log.error("Error parsing JSON request: " + e);
            messageSender.sendInvalidJSONRPCRequestFormat(id);
        } catch (Exception e) {
            Log.error("Error handling request: " + e);
            messageSender.sendInternalError(id, e.getMessage());
        }

        return false;
    }

    void handleListPrompts(int id) {
        var prompts = promptLoader.all();
        var response = PromptResponses.listPrompts(id, prompts);
        messageSender.send(response);
    }

    /*
     * https://modelcontextprotocol.io/specification/2025-03-26/server/prompts#
     * getting-a-prompt
     */
    void handleGetPrompt(int id, JSONObject json) {
        var params = json.getJSONObject("params");
        var promptName = params.getString("name");
        var prompt = this.promptLoader.get(promptName);
        if (prompt.isEmpty()) {
            Log.error("Prompt not found: " + promptName);
            messageSender.sendInvalidRequest(id, "Prompt not found: " + promptName);
            return;
        }

        var response = PromptResponses.getPrompt(id, prompt.get());
        messageSender.send(response.toString());
    }
}
