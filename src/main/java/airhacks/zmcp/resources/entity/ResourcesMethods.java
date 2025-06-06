package airhacks.zmcp.resources.entity;

import java.util.Optional;

import airhacks.zmcp.router.entity.RequestMethods;

/**
 * https://modelcontextprotocol.io/specification/2025-03-26/server/resources
 */
public enum ResourcesMethods implements RequestMethods {
    
    RESOURCES_LIST("resources/list"),
    RESOURCES_READ("resources/read"),
    SUBSCRIBE("resources/subscribe"),
    UNSUBSCRIBE("resources/unsubscribe"),
    NOTIFICATIONS_INITIALIZED("notifications/initialized"),
    NOTIFICATIONS_CANCELLED("notifications/cancelled");

    private final String method;

    ResourcesMethods(String method) {
        this.method = method;
    }

    public String method() {
        return method;
    }

    public static Optional<ResourcesMethods> fromString(String method) {
        for (ResourcesMethods protocol : values()) {
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