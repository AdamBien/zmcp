package airhacks.zmcp.prompts.control;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import airhacks.zmcp.log.boundary.Log;
import airhacks.zmcp.prompts.entity.PromptSignature;
import airhacks.zmcp.prompts.entity.PromptArgument;

public record PromptLoader(Path promptsDir) {


    public List<PromptSignature> all() {
        return List.of(
                new PromptSignature("code_review", "Asks the LLM to analyze code quality and suggest improvements",
                        List.of(new PromptArgument("code", "The code to review", true))));

    }

    record PromptFile(Path path,String content){

     static PromptFile from(Path path){
        try {
            var content = Files.readString(path);
            return new PromptFile(path,content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
     }
    }
    
    List<PromptFile> allPrompts(){
        try {
            return Files.list(this.promptsDir)
            .filter(Files::isRegularFile)
            .map(PromptFile::from)
            .toList();
        } catch (IOException e) {
            Log.error("Error reading prompts directory: " + e);
            return List.of();
        }
    }

    public Optional<PromptSignature> get(String name) {
        Log.info("getting prompt: " + name);
        return Optional.of(all().getFirst());
    }

}
