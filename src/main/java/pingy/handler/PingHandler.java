package pingy.handler;

import com.google.inject.Inject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pingy.service.PingService;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static pingy.handler.HttpParams.DURATION_BACK;
import static pingy.handler.HttpParams.PING_THRESHOLD;

public class PingHandler implements HttpHandler {

    private static final String DEFAULT_PING_THRESHOLD = "1";
    private static final String DEFAULT_DURATION_BACK = "" + (1000 * 60 * 2);

    private static final int HTTP_OK = 200;
    private static final Logger LOGGER = LoggerFactory.getLogger(PingHandler.class);
    private static final String POST_RESPONSE = "Received!";

    private final PingService pingService;

    private static final Function<Long, String> TIME_FORMATTER = millis -> "\"" + new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date(millis)) + "\"";

    @Inject
    public PingHandler(PingService pingService) {
        this.pingService = pingService;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        LOGGER.info("received something");

        switch (httpExchange.getRequestMethod()) {
            case "GET":
                get(httpExchange);
                break;
            case "POST":
                post(httpExchange);
                break;
            default:
                throw new RuntimeException("Unknown request method: " + httpExchange.getRequestMethod());
        }
    }

    private void post(HttpExchange httpExchange) throws IOException {
        LOGGER.info("received ping!");
        pingService.addPing();
        httpExchange.sendResponseHeaders(HTTP_OK, POST_RESPONSE.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(POST_RESPONSE.getBytes());
        os.close();
    }

    private void get(HttpExchange httpExchange) throws IOException {
        Map<String, String> parameters = queryToMap(httpExchange.getRequestURI().getQuery());
        LOGGER.info("received GET request with params: " + parameters);

        Long durationBack = Long.valueOf(parameters.getOrDefault(DURATION_BACK.getParamName(), DEFAULT_DURATION_BACK));
        int pingThreshold = Integer.valueOf(parameters.getOrDefault(PING_THRESHOLD.getParamName(), DEFAULT_PING_THRESHOLD));

        List<String> pings = pingService.getPings(durationBack).stream()
                .map(TIME_FORMATTER)
                .collect(toList());

        boolean isBusy = pings.size() >= pingThreshold;

        String response = "{ \"pings\": " + pings.toString() +
                ", \"isBusy\": " + isBusy + ", " +
                "\"durationBack\": " + (durationBack / 1000) +
        " }";

        httpExchange.sendResponseHeaders(HTTP_OK, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static Map<String, String> queryToMap(String query) {
        if (query == null) {
            return Collections.emptyMap();
        }
        Pattern p = Pattern.compile("&");
        return p.splitAsStream(query)
                .map(pair -> pair.split("="))
                .collect(Collectors.toMap(array -> array[0], array -> array.length > 1 ? array[1] : ""));
    }
}
