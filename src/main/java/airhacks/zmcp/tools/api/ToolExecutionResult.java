package airhacks.zmcp.tools.api;

public record ToolExecutionResult(String content, boolean error) {
    
    public static ToolExecutionResult success(String content) {
        return new ToolExecutionResult(content, false);
    }

    public static ToolExecutionResult error(String content) {
        return new ToolExecutionResult(content, true);
    }

    public boolean emptyContent(){
        return content.isEmpty();
    }

}
