package airhacks.zmcp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface JSONLoader {
    
    static String load(String component,String file)  {
        var path = Path.of("src/test/json", component, file + ".json");
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error loading JSON file: " + path, e);
        }
    }

    static String loadPromptResponse(String file)  {
        return load("promptresponses", file);
    }

}
