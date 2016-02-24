package pingy;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

public class PingServer {

    private static final String PORT = "8000";
    private static final Logger LOGGER = LoggerFactory.getLogger(PingServer.class);
    private final PingHandler pingHandler;

    @Inject
    public PingServer(PingHandler pingHandler) {
        this.pingHandler = pingHandler;
    }

    public void start(int serverPort) throws IOException {
        LOGGER.info("Starting server on port: " + serverPort);
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        server.createContext("/ping", pingHandler);
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    public static final void main(String[] args) throws IOException {
        Injector injector = Guice.createInjector(new PingModule());

        int serverPort = Integer.parseInt(System.getProperty("server.port", PORT));
        PingServer pingServer = injector.getInstance(PingServer.class);
        pingServer.start(serverPort);
    }
}
