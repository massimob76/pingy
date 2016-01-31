package pingy;

import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

public class PingServer {

    private static final String PORT = "8000";
    private static final Logger LOGGER = LoggerFactory.getLogger(PingServer.class);

    public void start(int serverPort) throws IOException {
        LOGGER.info("Starting server on port: " + serverPort);
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        server.createContext("/ping", new PingHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    public static final void main(String[] args) throws IOException {
        int serverPort = Integer.parseInt(System.getProperty("server.port", PORT));
        new PingServer().start(serverPort);
    }
}
