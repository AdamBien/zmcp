package airhacks.zmcp.tools.boundary;

import airhacks.zmcp.router.boundary.RequestHandler;
import airhacks.zmcp.router.entity.MCPRequest;

public class ToolsSTDIOProtocol implements RequestHandler{

    @Override
    public boolean handleRequest(MCPRequest request) {
        return false;
    }
    
}
