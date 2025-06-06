package airhacks.zmcp.router.entity;

import org.json.JSONObject;

    public record MCPRequest(int id, String method, JSONObject json) {

}
