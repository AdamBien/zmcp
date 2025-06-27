package airhacks.zmcp.prompts.control;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.prompts.entity.PromptInstance;
import airhacks.zmcp.prompts.entity.PromptSignature;

public record PromptLoader(Path promptsDir) {


    public List<PromptSignature> all() {
        return allPrompts().stream()
        .map(PromptInstance::signature)
        .toList();

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
            .map(PromptFile::from)
            .map(PromptFile::toPromptInstance)
            .toList();
        } catch (IOException e) {
            Log.error("Error reading prompts directory: " + e);
            return List.of();
        }
    }

    public Optional<PromptInstance> get(String name) {
        Log.info("getting prompt: " + name);
        return Optional.of(allPrompts().getFirst());
    }

}
