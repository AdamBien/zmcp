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

For detailed instructions on creating tools, see [TOOLS.md](TOOLS.md)

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