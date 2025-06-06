package airhacks.zmcp.router.boundary;

import java.util.Optional;

import airhacks.zmcp.router.entity.MCPRequest;

public interface RequestHandler {
    boolean handleRequest(MCPRequest request); 
    
    default Optional<String> capability(){
        return Optional.empty();
    }
}
