package airhacks.zmcp.prompts.control;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.prompts.entity.PromptInstance;
import airhacks.zmcp.prompts.entity.PromptSignature;

public record PromptLoader(Path promptsDir) {


    public List<PromptSignature> all() {
        return allPrompts().stream()
        .map(PromptInstance::signature)
        .toList();

    }
    static String nameFromPath(Path path){
        return path.getFileName()
        .toString()
        .replace(".json", "");
    }


    record PromptFile(Path path,String content){
     
    public PromptInstance toPromptInstance(){
        
        return PromptInstance.fromJson(this.content);
    }

     static PromptFile from(Path path){
        try {
            var content = Files.readString(path);
            return new PromptFile(path,content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
     }
    }
    
    List<PromptInstance> allPrompts(){
        try {
            return Files.list(this.promptsDir)
            .filter(Files::isRegularFile)
            .filter(isJSON())
            .map(PromptFile::from)
            .map(PromptFile::toPromptInstance)
            .toList();
        } catch (IOException e) {
            Log.error("Error reading prompts directory: " + e);
            return List.of();
        }
    }

    Predicate<? super Path> isJSON() {
        return path -> path.getFileName().toString().endsWith(".json");
    }

    public Optional<PromptInstance> get(String name) {
        Log.info("getting prompt: " + name);
        return allPrompts()
        .stream()
        .filter(prompt -> prompt.hasName(name))
        .findFirst();
    }

}
