package airhacks.zmcp.entity;

public record Resource(
    String uri,
    String name,
    String mimeType
) {
    public String toJson() {
        return String.format(
            """
            {
                "uri": "%s",
                "name": "%s",
                "mimeType": "%s"
            }""",
            uri(),
            name(),
            mimeType()
        );
    }
} 