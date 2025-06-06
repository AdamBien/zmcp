package airhacks.zmcp.router.boundary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import airhacks.zmcp.log.boundary.Log;

public class FrontDoor {

    RequestHandler requestHandler;

    public FrontDoor(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public void start() throws IOException {
        Log.info("Starting StdioTransport");
        try (var isr = new InputStreamReader(System.in); var reader = new BufferedReader(isr)) {
            while (true) {
                try {
                    Log.info("Waiting for next message...");
                    var line = reader.readLine();
                    if (line == null) {
                        Log.info("Connection closed by client ");
                        break;
                    }
                    if (line.isBlank()) {
                        Log.info("Received blank line, skipping");
                        continue;
                    }
                    Log.request(line);
                    handleRequest(line);
                } catch (IOException e) {
                    Log.error("Error reading from input: " + e);
                    break;
                }
            }
        } finally {
            Log.info("StdioTransport stopped");
        }
    }

    void handleRequest(String request) {
        this.requestHandler.handleRequest(request);
    }

}
