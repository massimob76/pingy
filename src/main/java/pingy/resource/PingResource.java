package pingy.resource;

import static java.util.stream.Collectors.toList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pingy.service.PingService;

@Path("/ping")
public class PingResource {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PingResource.class);
    
    private static final int DEFAULT_PING_THRESHOLD = 1;
    private static final long DEFAULT_DURATION_BACK = 1000 * 60 * 2;
    
    private static final Function<Long, String> TIME_FORMATTER = millis -> "\"" + new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date(millis)) + "\"";

    private final PingService pingService;

    @Inject
    public PingResource(PingService pingService) {
        this.pingService = pingService;
    }

    @POST
    public String doPing() {
        LOGGER.info("Received new ping");
        pingService.addPing();
        return "Received!";
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getPings(@QueryParam("pingThreshold") Integer pingThresholdParam, 
                           @QueryParam("durationBack") Long durationBackParam) {
        LOGGER.info("Received GET request with pingThreshold {} and durationBack {}", pingThresholdParam, durationBackParam);

        long durationBack = durationBackParam != null ? durationBackParam : DEFAULT_DURATION_BACK;
        List<String> pings = pingService.getPings(durationBack).stream()
                .map(TIME_FORMATTER)
                .collect(toList());

        int pingThreshold = pingThresholdParam != null ? pingThresholdParam : DEFAULT_PING_THRESHOLD;
        boolean isBusy = pings.size() >= pingThreshold;

        return "{ \"pings\": " + pings.toString() +
                ", \"isBusy\": " + isBusy + ", " +
                "\"durationBack\": " + (durationBack / 1000) +
        " }";
    }

}
