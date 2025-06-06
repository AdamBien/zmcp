package airhacks;

import java.io.IOException;

import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.resources.boundary.ResourcesSTDIOProtocol;
import airhacks.zmcp.router.boundary.FrontDoor;


/**
 *
 * @author airhacks.com
 */
public interface App {

    String VERSION = "zmcp v2025.06.06.03"; 


    static void main(String... args) throws IOException {
        Log.init();
        var frontDoor = new FrontDoor(new ResourcesSTDIOProtocol("."));
        frontDoor.start();
    }
}
