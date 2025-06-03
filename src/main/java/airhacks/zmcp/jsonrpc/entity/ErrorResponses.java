package airhacks.zmcp.jsonrpc.entity;

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
        return """
            {
                "jsonrpc": "2.0",
                "id": %d,
                "error": {
                    "code": %d,
                    "message": "%s"
                }
            }"""
            .formatted(id, code, message);
    }
}
