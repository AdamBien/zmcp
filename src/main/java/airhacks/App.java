package airhacks;

import java.io.IOException;

import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.resources.boundary.StdioTransport;


/**
 *
 * @author airhacks.com
 */
interface App {

    static void main(String... args) throws IOException {
        Log.init();
        var transport = new StdioTransport();
        transport.start();
    }
}
