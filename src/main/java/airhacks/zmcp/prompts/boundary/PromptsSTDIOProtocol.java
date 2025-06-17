package airhacks.zmcp.prompts.boundary;

import java.util.Optional;

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
        return false;
    }
}
