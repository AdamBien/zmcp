package airhacks.zmcp.jsonrpc.entity;

import org.json.JSONObject;

public interface ErrorResponses {

    static String unsupportedProtocolVersion(int id, String message,String supported,String requested) {

        return """
                        {
                            "jsonrpc": "2.0",
                            "id": %d,
                            "error": {
                                "code":  -32602,
                                "message": "%s"
                            },
                             "data": {
                                "supported": ["%s"],
                                "requested": "%s"
                }
                        }"""
                .formatted(id, message,supported,requested);
    }

    static String error(int id, int code, String message) {
        var jsonObject = new JSONObject();
        jsonObject.put("jsonrpc", "2.0");
        jsonObject.put("id", id);
        
        var errorObject = new JSONObject();
        errorObject.put("code", code);
        errorObject.put("message", message);
        jsonObject.put("error", errorObject);
        
        return jsonObject.toString();
    }
}
