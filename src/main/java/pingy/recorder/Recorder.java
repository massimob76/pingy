package pingy.recorder;

import com.google.inject.Inject;
import pingy.TimeProvider;

import java.util.ArrayList;
import java.util.List;

public class Recorder {

    private final TimeProvider timeProvider;

    @Inject
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
}
