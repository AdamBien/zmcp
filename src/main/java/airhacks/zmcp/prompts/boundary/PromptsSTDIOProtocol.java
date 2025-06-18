package airhacks.zmcp.prompts.boundary;

import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;

import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.prompts.entity.PromptsMethods;
import airhacks.zmcp.resources.control.MessageSender;
import airhacks.zmcp.router.boundary.RequestHandler;
import airhacks.zmcp.router.entity.MCPRequest;

public class PromptsSTDIOProtocol implements RequestHandler {

    MessageSender messageSender;

    public PromptsSTDIOProtocol() {
        this.messageSender = new MessageSender();

    }

    public Optional<String> capability() {
        return Optional.of("""
           "capabilities": {
                    "prompts": {
                    "listChanged": true
                    
                }
            }    
        """);
    }

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
                case PROMPTS_GET -> handleGetPrompt(id,json);
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

    }

    void handleGetPrompt(int id, JSONObject json) {

    }
}
