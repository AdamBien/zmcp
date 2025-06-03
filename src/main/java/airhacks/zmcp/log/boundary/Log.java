package airhacks.zmcp.log.boundary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Writes messages to zmcp.log file
 */
public interface Log {

    static void init(){
        try {
            Files.writeString(Path.of("zmcp.log"),  "zmcp started \n", StandardOpenOption.CREATE);
        } catch (IOException e) {
        }
    }
    
    static void info(String message){
        try {
            Files.writeString(Path.of("zmcp.log"), message + "\n", StandardOpenOption.APPEND);
        } catch (IOException e) {
        }
    }
}
