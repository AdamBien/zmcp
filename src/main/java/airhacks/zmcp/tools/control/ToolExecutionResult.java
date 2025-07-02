package airhacks.zmcp.tools.control;

import java.util.Map;

public record ToolExecutionResult(String content, boolean error) {

    public ToolExecutionResult(String content, String error) {
        this(content,Boolean.valueOf(error));
    }
    
    public static ToolExecutionResult success(String content) {
        return new ToolExecutionResult(content, false);
    }

    public static ToolExecutionResult error(String content) {
        return new ToolExecutionResult(content, true);
    }

    public boolean emptyContent(){
        return content.isEmpty();
    }

    public static ToolExecutionResult of(Map<String,String> result) {
        return new ToolExecutionResult(result.get("content"), result.get("error"));
    }

}
