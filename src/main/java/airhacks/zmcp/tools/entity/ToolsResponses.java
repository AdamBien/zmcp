package airhacks.zmcp.tools.entity;

public interface ToolsResponses {

    static String listTools(int id) {
        return """
            {
            "jsonrpc": "2.0",
            "id": %d,
            "result": {
                "tools": [
                {
                    "name": "get_weather",
                    "description": "Get current weather information for a location",
                    "inputSchema": {
                    "type": "object",
                    "properties": {
                        "location": {
                        "type": "string",
                        "description": "City name or zip code"
                        }
                    },
                    "required": ["location"]
                    }
                }
                ]
            }
        }                
         """.formatted(id);
    }
}
