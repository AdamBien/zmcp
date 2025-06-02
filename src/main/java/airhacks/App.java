package airhacks;

import com.sun.net.httpserver.HttpServer;
import airhacks.zmcp.boundary.ResourceBoundary;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 *
 * @author airhacks.com
 */
interface App {

    static void main(String... args) throws IOException {
        var server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/resources", new ResourceBoundary());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8080");
    }
}
