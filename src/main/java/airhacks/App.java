package airhacks;

import java.io.IOException;
import java.util.List;

import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.prompts.boundary.PromptsSTDIOProtocol;
import airhacks.zmcp.resources.boundary.ResourcesSTDIOProtocol;
import airhacks.zmcp.router.boundary.FrontDoor;
import airhacks.zmcp.tools.boundary.ToolsSTDIOProtocol;


/**
 *
 * @author airhacks.com
 */
public interface App {

    String VERSION = "zmcp v2025.06.23.01";     

    String RESOURCE_DIR =".";
    String PROMPTS_DIR = "./prompts";

    record Arguments(String resourceDir,String promptsDir){

        static Arguments from(String... args){
            return new Arguments(
                args.length > 0 ? args[0]: RESOURCE_DIR,
                args.length > 1 ? args[1] : PROMPTS_DIR);
              
        }
    }

    static void main(String... args) throws IOException {
        Log.init();
        var arguments = Arguments.from(args);
        var frontDoor = new FrontDoor(List.of(
            new ResourcesSTDIOProtocol(arguments.resourceDir()),
            new ToolsSTDIOProtocol(),
            new PromptsSTDIOProtocol(arguments.promptsDir())));
        frontDoor.start();
    }
}
