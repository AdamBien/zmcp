package airhacks.zmcp.resources.entity;

import java.util.Optional;

public enum ResourcesMethods {
    INITIALIZE("initialize"),
    INITIALIZED("initialized"),
    LIST_RESOURCES("resources/list"),
    READ_RESOURCE("resources/read"),
    SUBSCRIBE("resources/subscribe"),
    UNSUBSCRIBE("resources/unsubscribe");

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