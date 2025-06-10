package airhacks.zmcp.resources.control;

import java.io.PrintWriter;

import airhacks.zmcp.jsonrpc.entity.ErrorResponses;
import airhacks.zmcp.log.boundary.Log;

public class MessageSender {
    PrintWriter writer;

    public MessageSender() {
        this.writer = new PrintWriter(System.out, true);
    }

    public void sendError(Integer id, int code, String message) {
        Log.info("Sending error response");
        this.send(ErrorResponses.error(id, code, message));
    }
    public void methodNotImplementedYet(Integer id) {
        sendError(id, -32601, "Method not implemented yet");
    }


    public void send(String jsonMessage) {
        var strippedMessage = jsonMessage.replaceAll("\\s+", "");
        Log.response(strippedMessage);
        writer.println(strippedMessage);
    }

    public void sendInvalidJSONRPCRequestFormat(Integer id) {     
        sendError(id, -32700, "Invalid JSON-RPC request format");
    }

    public void sendInternalError(Integer id, String message) {
        sendError(id, -32603, "Internal error: " + message);
    }

    public void sendInvalidRequest(int id, String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendInvalidRequest'");
    }
}
