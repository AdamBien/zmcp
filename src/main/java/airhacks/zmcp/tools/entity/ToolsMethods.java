package airhacks.zmcp.tools.entity;

import java.util.Optional;

import airhacks.zmcp.router.entity.RequestMethods;

/**
 * https://modelcontextprotocol.io/specification/2025-03-26/server/tools#protocol-messages
 */
public enum ToolsMethods implements RequestMethods {
    
    TOOLS_LIST("tools/list"), 
    TOOLS_CALL("tools/call");

    private final String method;

    ToolsMethods(String method) {
        this.method = method;
    }

    public String method() {
        return method;
    }

    public static Optional<ToolsMethods> fromString(String method) {
        for (var protocol : values()) {
            if (protocol.method.equals(method)) {
                return Optional.of(protocol);
            }
        }
        return Optional.empty();
    }

    public boolean isMethod(String method) {
        return this.method().equals(method);
    }
}