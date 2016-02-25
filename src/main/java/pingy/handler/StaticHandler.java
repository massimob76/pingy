package pingy.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.InputStream;
import java.io.OutputStream;

@Singleton
public class StaticHandler implements HttpHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(StaticHandler.class);

    private static final int HTTP_OK = 200;

    @Override
    public void handle(HttpExchange httpExchange) {

        try {
            LOGGER.info("In static handler!");

            httpExchange.getResponseHeaders().set("Content-Type", "text/html");
            httpExchange.sendResponseHeaders(200, 0);

            OutputStream os = httpExchange.getResponseBody();
            InputStream fs = this.getClass().getResourceAsStream("/web/ping.html");
            final byte[] buffer = new byte[0x10000];
            int count = 0;
            while ((count = fs.read(buffer)) >= 0) {
                os.write(buffer, 0, count);
            }
            fs.close();
            os.close();

            LOGGER.info("Static handler done!");
        } catch (Exception e) {
            LOGGER.error("Something", e);
        }
    }
}
