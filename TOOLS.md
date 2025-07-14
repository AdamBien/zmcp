# Creating a zmcp Tool

For a quick start, use the template project: [zmcp-tool](https://github.com/AdamBien/zmcp-tool)

## Specifications

- [MCP Server Tools Specification](https://modelcontextprotocol.io/specification/2025-03-26/server/tools)
- [JSON Schema](https://json-schema.org/) - for input validation
- [Java Service Provider Interface (SPI)](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/ServiceLoader.html) - for tool discovery

## Steps

1. **Create tool class** implementing `Function<Map<String,Object>, Map<String,String>>`

2. **Add TOOL_SPEC field** with tool metadata and [JSON schema](https://json-schema.org/learn/getting-started-step-by-step):
   ```java
   public static final Map<String, String> TOOL_SPEC = Map.of(
       "name", "your-tool-name",
       "description", "Tool description",
       "inputSchema", """
           {
               "type": "object",
               "properties": {
                   "param1": {
                       "type": "string",
                       "description": "Parameter description"
                   }
               },
               "required": ["param1"]
           }
           """
   );
   ```

3. **Implement apply method**:
   ```java
   @Override
   public Map<String, String> apply(Map<String, Object> input) {
       // Your tool logic here
       return Map.of("content", "result");
   }
   ```

4. **Register via SPI** - Create `META-INF/services/java.util.function.Function` file with your tool's fully qualified class name (see [ServiceLoader documentation](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/ServiceLoader.html))

5. **Build and test** - Tools are auto-discovered at runtime

## Example Structure
```
src/main/java/your/package/YourTool.java
src/main/resources/META-INF/services/java.util.function.Function
```

## Complete Example

```java
package your.package;

import java.util.Map;
import java.util.function.Function;

public class ExampleTool implements Function<Map<String, Object>, Map<String, String>> {

    public static final Map<String, String> TOOL_SPEC = Map.of(
        "name", "example-tool",
        "description", "An example tool that processes text input",
        "inputSchema", """
            {
                "type": "object",
                "properties": {
                    "text": {
                        "type": "string",
                        "description": "The text to process"
                    },
                    "uppercase": {
                        "type": "boolean",
                        "description": "Whether to convert to uppercase",
                        "default": false
                    }
                },
                "required": ["text"]
            }
            """
    );

    @Override
    public Map<String, String> apply(Map<String, Object> input) {
        var text = (String) input.get("text");
        var uppercase = (Boolean) input.getOrDefault("uppercase", false);
        
        var result = uppercase ? text.toUpperCase() : text;
        return Map.of("content", result);
    }
}
```