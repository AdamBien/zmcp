package airhacks.zmcp.router.boundary;

import airhacks.zmcp.router.entity.MCPRequest;

public interface RequestHandler {
    void handleRequest(MCPRequest request); 
}
