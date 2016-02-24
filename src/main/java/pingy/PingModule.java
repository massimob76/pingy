package pingy;

import com.google.inject.AbstractModule;

public class PingModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TimeProvider.class).toProvider(() -> () -> System.currentTimeMillis());
    }
}
