package airhacks.zmcp.log.boundary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import airhacks.App;

/**
 * Writes messages to zmcp.log file
 */
public interface Log {

    static final Path LOG_FILE = Path.of("zmcp.log");

    static void init(){
        try {
            Files.writeString(LOG_FILE,  "🚀🧐 %s started %n".formatted(App.VERSION), StandardOpenOption.CREATE);
        } catch (IOException e) {
        }
    }

    static void info(String message){
        append("💡 " + message);
    }

    static void debug(String message){
        append("🐛 [%s] %s".formatted(clazzName(), message));
    }

    static void request(String message){
        append("👉 " + message);
    }

    static void response(String message){
        append("👈 " + message);
    }

    static void error(String message){
        append("❌ " + message);
    }

    static String clazzName(){
        return Thread.currentThread().getStackTrace()[1].getClassName();
    }


    private static void append(String message){
        try {
            Files.writeString(LOG_FILE, message + "\n", StandardOpenOption.APPEND);
        } catch (IOException e) {
        }
    }
}
