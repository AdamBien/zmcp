package airhacks.zmcp.router.boundary;


import java.util.Optional;

import airhacks.zmcp.router.entity.Capability;
import airhacks.zmcp.router.entity.MCPRequest;

public interface RequestHandler {
    boolean handleRequest(MCPRequest request); 
    
    default Optional<Capability> capability(){
        return Optional.empty();
    }
}
