package airhacks.zmcp.resources.entity;

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

    public static ResourcesMethods fromString(String method) {
        for (ResourcesMethods protocol : values()) {
            if (protocol.method.equals(method)) {
                return protocol;
            }
        }
        throw new IllegalArgumentException("Unknown method: " + method);
    }
}