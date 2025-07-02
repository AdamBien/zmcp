package airhacks;

import java.io.IOException;
import java.nio.file.Path;
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

    String VERSION = "zmcp v2025.07.02.01";     

    Path RESOURCE_DIR = Path.of(".");
    Path PROMPTS_DIR = Path.of("./prompts");

    record Arguments(Path resourceDir,Path promptsDir){

        static Arguments from(String... args){
            return new Arguments(
                args.length > 0 ? Path.of(args[0]) : RESOURCE_DIR,
                args.length > 1 ? Path.of(args[1]) : PROMPTS_DIR);
              
        }

        void userInfo(){
            Log.info("resources: %s prompts directory: %s".formatted(resourceDir(),promptsDir()));
        }
    }

    static void main(String... args) throws IOException {
        Log.init();
        var arguments = Arguments.from(args);
        arguments.userInfo();
        var frontDoor = new FrontDoor(List.of(
            new ResourcesSTDIOProtocol(arguments.resourceDir()),
            new ToolsSTDIOProtocol(),
            new PromptsSTDIOProtocol(arguments.promptsDir())));
        frontDoor.start();
    }
}
