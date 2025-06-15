package airhacks.zmcp.jsonrpc.entity;

public interface JsonRPCResponses {

    static String header(int id) {
        return """
                "jsonrpc": "2.0",
                "id": %d,
            }"""
            .formatted(id);
    }
}
