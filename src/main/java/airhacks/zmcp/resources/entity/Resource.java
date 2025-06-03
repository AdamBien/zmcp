package airhacks.zmcp.resources.entity;

public record Resource(
        String uri,
        String name,
        String mimeType) {
    public String toJson() {
        return """
                {
                    "uri": "%s",
                    "name": "%s",
                    "mimeType": "%s"
                }""".formatted(uri(), name(), mimeType())
                .replaceAll("\\s+", "");
    }
}