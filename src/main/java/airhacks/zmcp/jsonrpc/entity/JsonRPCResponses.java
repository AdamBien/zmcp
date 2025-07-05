package airhacks.zmcp.jsonrpc.entity;

public interface JsonRPCResponses {

    static String header(int id) {
        var jsonObject = new org.json.JSONObject();
        jsonObject.put("jsonrpc", "2.0");
        jsonObject.put("id", id);
        return jsonObject.toString();
    }
}
