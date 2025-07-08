# Creating a zmcp Tool

## Steps

1. **Create tool class** implementing `Function<Map<String,Object>, Map<String,String>>`

2. **Add TOOL_SPEC field** with tool metadata and JSON schema:
   ```java
   public static final Map<String, Object> TOOL_SPEC = Map.of(
       "name", "your-tool-name",
       "description", "Tool description",
       "inputSchema", Map.of(
           "type", "object",
           "properties", Map.of(
               "param1", Map.of("type", "string", "description", "Parameter description")
           ),
           "required", List.of("param1")
       )
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

4. **Register via SPI** - Create `META-INF/services/java.util.function.Function` file with your tool's fully qualified class name

5. **Build and test** - Tools are auto-discovered at runtime

## Example Structure
```
src/main/java/your/package/YourTool.java
src/main/resources/META-INF/services/java.util.function.Function
```