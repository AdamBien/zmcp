# zmcp
pure Java / zero dependency [MCP](https://modelcontextprotocol.io) implementation

A lightweight, zero-dependency implementation of the Model Context Protocol (MCP) in pure Java 21+.

![MCP Duke](mcpduke.png)


## Features

- Support for [**Prompts**](https://modelcontextprotocol.io/specification/2025-06-18/server/prompts) - define reusable prompt templates with arguments
- Access to [**Resources**:](https://modelcontextprotocol.io/specification/2025-06-18/server/resources) - read and list files and directories
- Integration with [**Tools**:](https://modelcontextprotocol.io/specification/2025-06-18/server/tools) - execute custom functions and operations

## Tool Plugins

MCP tools can be created as standalone JARs, independent of this project. 

You can find a template/quick starter here: [zmcp tool](https://github.com/adamBien/zmcp-tool)

Example tool implementations:
- [zmcp-code-executor](https://github.com/AdamBien/zmcp-code-executor) - Execute code snippets
- [zmcp-linkinfo-tool](https://github.com/AdamBien/zmcp-linkinfo-tool) - Extract link information

For detailed instructions on creating tools, see [TOOLS.md](TOOLS.md)

## Installation for Claude Desktop

1. **Build the project** to create the JAR file:
   ```bash
   mvn clean package
   ```
   This creates `target/zmcp.jar` with all dependencies included.

   **Optional**: Use [zb](https://github.com/adamBien/zb) for faster builds:
   ```bash
   zb.sh
   ```
   This creates a fat JAR in < 1s in the `.zbo` directory.

2. **Set up the directory structure**:
   ```bash
   mkdir ~/zmcp
   cp target/zmcp.jar ~/zmcp/
   cp zmcp.sh ~/zmcp/
   chmod +x ~/zmcp/zmcp.sh
   ```

3. **Configure Claude Desktop** by adding zmcp to your MCP settings:
   - Open Claude Desktop settings
   - Add to your MCP configuration:
   ```json
   {
     "mcpServers": {
       "zmcp": {
         "command": "/Users/[your-username]/zmcp/zmcp.sh",
         "args": ["/path/to/resources", "/path/to/prompts"]
       }
     }
   }
   ```

The `zmcp.sh` script:
- Launches Java with the zmcp JAR from the same directory
- Passes optional arguments for resource and prompt directories
- Default directories: `.` for resources, `./prompts` for prompts

The arguments are:
- First argument: Resource directory path (default: current directory)
- Second argument: Prompts directory path (default: ./prompts)

# Key Architectural Decisions

  1. Plugin Architecture via Java SPI

  - Uses Java Service Provider Interface for dynamic tool discovery
  - Tools implement `Function<Map<String,Object>, Map<String, String>>`
  - Enables runtime tool loading without hard dependencies

  2. Self-Describing Tools

  - Each tool has a TOOL_SPEC field with metadata and JSON schema
  - Allows the zmcp system to introspect tools via reflection
  - Follows convention-over-configuration pattern

  3. Functional Design

  - Tools are stateless functions (input string → output map)
  - Aligns with serverless/FaaS architectures
  - Clear separation: `boundary` (external-facing) vs `entity` (domain objects)

  4. Minimal Dependencies

  - Zero runtime dependencies
  - Java 21 target (leverages modern features like records)
  - Maven build with assembly plugin for fat JAR distribution
  - [zb](https://github.com/adamBien/zb) build creates a fat JAR in < 1s

  The architecture prioritizes extensibility, discoverability, and
  lightweight integration - ideal for a plugin system where tools can be
  added/removed without modifying the core framework.