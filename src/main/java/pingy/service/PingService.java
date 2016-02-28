package pingy.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import pingy.TimeProvider;
import pingy.recorder.Recorder;

@Singleton
public class PingService {

    private final Recorder recorder;
    private final TimeProvider timeProvider;

    @Inject
    public PingService(TimeProvider timeProvider) {
        this.recorder = new Recorder(timeProvider);
        this.timeProvider = timeProvider;
    }

    public void addPing() {
        recorder.ping();
    }

    public List<Long> getPings(Long durationBack) {
        if (durationBack == null) {
            return recorder.getAllPings();
        }

        long since = timeProvider.getCurrentTime() - durationBack;
        return recorder.getAllPings().stream()
                .filter(time -> time >= since)
                .collect(toList());
    }

}
