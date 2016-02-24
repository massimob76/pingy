package pingy.recorder;

import pingy.TimeProvider;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Recorder {

    private final TimeProvider timeProvider;

    public Recorder(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    private List<Long> pings = new ArrayList<>();

    public synchronized void ping() {
        pings.add(timeProvider.getCurrentTime());
    }

    public synchronized List<Long> getAllPings() {
        return pings;
    }

    public synchronized List<Long> getAllPingsSince(long since) {
        return pings.stream()
                .filter(time -> time >= since)
                .collect(toList());
    }
}
