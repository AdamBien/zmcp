package airhacks;

import java.io.IOException;

import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.resources.boundary.ResourcesProtocol;


/**
 *
 * @author airhacks.com
 */
public interface App {

    String VERSION = "zmcp v2025.06.04.02"; 


    static void main(String... args) throws IOException {
        Log.init();
        var transport = new ResourcesProtocol();
        transport.start();
    }
}
