package airhacks.zmcp.jsonrpc.entity;

import org.json.JSONObject;

public interface JsonRPCResponses {

    static JSONObject header(int id) {
        var jsonObject = new org.json.JSONObject();
        jsonObject.put("jsonrpc", "2.0");
        jsonObject.put("id", id);
        return jsonObject;
    }
}
