package pingy;

import com.google.inject.Inject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pingy.recorder.Recorder;

import java.io.IOException;
import java.io.OutputStream;

public class PingHandler implements HttpHandler {

    private static final int HTTP_OK = 200;
    private static final Logger LOGGER = LoggerFactory.getLogger(PingHandler.class);
    private static final String RESPONSE = "Received!";
    private final Recorder recorder;

    @Inject
    public PingHandler(Recorder recorder) {
        this.recorder = recorder;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        LOGGER.info("received ping!");
        recorder.ping();
        httpExchange.sendResponseHeaders(HTTP_OK, RESPONSE.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(RESPONSE.getBytes());
        os.close();
    }
}
