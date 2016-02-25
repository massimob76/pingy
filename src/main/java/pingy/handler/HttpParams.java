package pingy.handler;

public enum HttpParams {

    DURATION_BACK("durationBack"),
    PING_THRESHOLD("pingThreshold");

    private final String paramName;

    private HttpParams(String paramName) {
        this.paramName = paramName;
    }

    public String getParamName() {
        return paramName;
    }
}
