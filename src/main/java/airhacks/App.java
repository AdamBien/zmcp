package airhacks;

import java.io.IOException;
import java.util.List;

import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.resources.boundary.ResourcesSTDIOProtocol;
import airhacks.zmcp.router.boundary.FrontDoor;
import airhacks.zmcp.tools.boundary.ToolsSTDIOProtocol;


/**
 *
 * @author airhacks.com
 */
public interface App {

    String VERSION = "zmcp v2025.06.17.02";     

    static void main(String... args) throws IOException {
        Log.init();
        var frontDoor = new FrontDoor(List.of(
            new ResourcesSTDIOProtocol("."),
            new ToolsSTDIOProtocol()));
        frontDoor.start();
    }
}
