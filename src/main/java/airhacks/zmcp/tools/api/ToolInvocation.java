package airhacks.zmcp.tools.api;

/**
 * https://modelcontextprotocol.io/specification/2025-03-26/server/tools#calling-tools
 * 
 * ToolInvocation is a functional interface that represents a tool invocation initiated by the client / LLM.
 */
public interface ToolInvocation {
    
    ToolExecutionResult use(String parameters);
}
