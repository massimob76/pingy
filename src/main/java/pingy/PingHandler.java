package pingy;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class PingHandler implements HttpHandler {

    private static final int HTTP_OK = 200;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Received!";
        httpExchange.sendResponseHeaders(HTTP_OK, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
