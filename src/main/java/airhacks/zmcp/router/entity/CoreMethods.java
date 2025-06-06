package airhacks.zmcp.router.entity;

import java.util.Optional;

public enum CoreMethods implements RequestMethods {
    INITIALIZE("initialize"),
    INITIALIZED("initialized");

    private final String method;

    CoreMethods(String method) {
        this.method = method;
    }

    public static Optional<CoreMethods> fromString(String method) {
        for (CoreMethods protocol : values()) {
            if (protocol.method.equals(method)) {
                return Optional.of(protocol);
            }
        }
        return Optional.empty();
    }
    public String method() {
        return method;
    }
}
