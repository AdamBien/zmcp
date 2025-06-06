package airhacks.zmcp.router.entity;

public interface RequestMethods {
    
    public String method();
    
    default boolean isMethod(String method) {
        return this.method().equals(method);
    }
}
