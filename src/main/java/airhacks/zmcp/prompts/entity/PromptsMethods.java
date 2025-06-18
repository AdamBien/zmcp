package airhacks.zmcp.prompts.entity;

import java.util.Optional;

import airhacks.zmcp.router.entity.RequestMethods;

/**
 * https://modelcontextprotocol.io/specification/2025-03-26/server/prompts#protocol-messages
 */
public enum PromptsMethods implements RequestMethods {
    
    PROMPTS_LIST("prompts/list"),
    PROMPTS_GET("prompts/get");
    
    String method;

    PromptsMethods(String method) {
        this.method = method;
    }

    public String method() {
        return method;
    }
    
    public static Optional<PromptsMethods> fromString(String method) {
        for (PromptsMethods protocol : values()) {
            if (protocol.method.equals(method)) {
                return Optional.of(protocol);
            }
        }
        return Optional.empty();
    }
}
