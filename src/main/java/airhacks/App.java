package airhacks;

import java.io.IOException;

import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.resources.boundary.StdioTransport;


/**
 *
 * @author airhacks.com
 */
public interface App {

    String VERSION = "zmcp v2025.06.03.7";


    static void main(String... args) throws IOException {
        Log.init();
        var transport = new StdioTransport();
        transport.start();
    }
}
