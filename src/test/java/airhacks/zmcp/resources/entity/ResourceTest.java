package airhacks.zmcp.resources.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import airhacks.zmcp.JSONAssertions;

public class ResourceTest {

    @Test
    public void toJson() {
        var resource = new Resource("file:///home/duke/Coffee.java", "Coffee.java", "Coffee", "text/plain");
        var actual = resource.toJson();
        var expected = """
                   {
                  "uri": "file:///home/duke/Coffee.java",
                  "name": "Coffee.java",
                  "title": "Coffee",
                  "mimeType": "text/plain"
                }
                  """;

        JSONAssertions.assertEquals(actual,
                expected);
    }

    @Test
    public void fromPathWithJavaFile(@TempDir Path tempDir) throws IOException {
        var javaFile = tempDir.resolve("Example.java");
        Files.writeString(javaFile, "public class Example {}");
        
        var resource = Resource.fromPath(javaFile);
        
        assertThat(resource.uri()).startsWith("file:");
        assertThat(resource.uri()).endsWith("Example.java");
        assertThat(resource.name()).isEqualTo("Example.java");
        assertThat(resource.title()).isEqualTo("Example.java");
        assertThat(resource.mimeType()).isEqualTo("text/plain");
    }

    @Test
    public void fromPathWithTextFile(@TempDir Path tempDir) throws IOException {
        var textFile = tempDir.resolve("readme.txt");
        Files.writeString(textFile, "This is a readme file");
        
        var resource = Resource.fromPath(textFile);
        
        assertThat(resource.uri()).startsWith("file:");
        assertThat(resource.uri()).endsWith("readme.txt");
        assertThat(resource.name()).isEqualTo("readme.txt");
        assertThat(resource.title()).isEqualTo("readme.txt");
        assertThat(resource.mimeType()).isEqualTo("text/plain");
    }

    @Test
    public void fromPathWithHtmlFile(@TempDir Path tempDir) throws IOException {
        var htmlFile = tempDir.resolve("index.html");
        Files.writeString(htmlFile, "<html><body>Hello</body></html>");
        
        var resource = Resource.fromPath(htmlFile);
        
        assertThat(resource.uri()).startsWith("file:");
        assertThat(resource.uri()).endsWith("index.html");
        assertThat(resource.name()).isEqualTo("index.html");
        assertThat(resource.title()).isEqualTo("index.html");
        assertThat(resource.mimeType()).isEqualTo("text/html");
    }

    @Test
    public void fromPathWithXmlFile(@TempDir Path tempDir) throws IOException {
        var xmlFile = tempDir.resolve("config.xml");
        Files.writeString(xmlFile, "<?xml version=\"1.0\"?><config></config>");
        
        var resource = Resource.fromPath(xmlFile);
        
        assertThat(resource.uri()).startsWith("file:");
        assertThat(resource.uri()).endsWith("config.xml");
        assertThat(resource.name()).isEqualTo("config.xml");
        assertThat(resource.title()).isEqualTo("config.xml");
        assertThat(resource.mimeType()).isIn("text/xml", "application/xml");
    }

    @Test
    public void fromPathWithJsonFile(@TempDir Path tempDir) throws IOException {
        var jsonFile = tempDir.resolve("data.json");
        Files.writeString(jsonFile, "{}");
        
        var resource = Resource.fromPath(jsonFile);
        
        assertThat(resource.uri()).startsWith("file:");
        assertThat(resource.uri()).endsWith("data.json");
        assertThat(resource.name()).isEqualTo("data.json");
        assertThat(resource.title()).isEqualTo("data.json");
        assertThat(resource.mimeType()).isEqualTo("application/json");
    }

    @Test
    public void fromPathWithUnknownFileType(@TempDir Path tempDir) throws IOException {
        var unknownFile = tempDir.resolve("data.xyz");
        Files.writeString(unknownFile, "some content");
        
        var resource = Resource.fromPath(unknownFile);
        
        assertThat(resource.uri()).startsWith("file:");
        assertThat(resource.uri()).endsWith("data.xyz");
        assertThat(resource.name()).isEqualTo("data.xyz");
        assertThat(resource.title()).isEqualTo("data.xyz");
        assertThat(resource.mimeType()).isIn(null, "text/plain");
    }

    @Test
    public void toJsonPreservesAllFields() {
        var resource = new Resource(
            "postgres://database/customers/schema",
            "customers",
            "Customer Database Schema",
            "application/sql"
        );
        
        var json = resource.toJson();
        
        assertThat(json).contains("\"uri\":\"postgres://database/customers/schema\"");
        assertThat(json).contains("\"name\":\"customers\"");
        assertThat(json).contains("\"title\":\"Customer Database Schema\"");
        assertThat(json).contains("\"mimeType\":\"application/sql\"");
    }

    @Test
    public void fromPathHandlesPathWithSpaces(@TempDir Path tempDir) throws IOException {
        var fileWithSpaces = tempDir.resolve("my document.txt");
        Files.writeString(fileWithSpaces, "content");
        
        var resource = Resource.fromPath(fileWithSpaces);
        
        assertThat(resource.uri()).contains("my%20document.txt");
        assertThat(resource.name()).isEqualTo("my document.txt");
        assertThat(resource.title()).isEqualTo("my document.txt");
    }

    @Test
    public void fromPathHandlesSpecialCharacters(@TempDir Path tempDir) throws IOException {
        var fileWithSpecialChars = tempDir.resolve("test@file#1.txt");
        Files.writeString(fileWithSpecialChars, "content");
        
        var resource = Resource.fromPath(fileWithSpecialChars);
        
        assertThat(resource.name()).isEqualTo("test@file#1.txt");
        assertThat(resource.title()).isEqualTo("test@file#1.txt");
        assertThat(resource.uri()).startsWith("file:");
    }
}
