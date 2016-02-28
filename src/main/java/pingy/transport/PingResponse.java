package pingy.transport;

import java.util.List;

public class PingResponse {

    private List<String> pings;
    private boolean isBusy;
    private long durationBack;

    public PingResponse(List<String> pings, boolean isBusy, long durationBackInSeconds) {
        this.pings = pings;
        this.isBusy = isBusy;
        this.durationBack = durationBackInSeconds;   
    }
    
    public List<String> getPings() {
        return pings;
    }
    
    public boolean getIsBusy() {
        return isBusy;
    }
    
    public long getDurationBack() {
        return durationBack;
    }
    
}
