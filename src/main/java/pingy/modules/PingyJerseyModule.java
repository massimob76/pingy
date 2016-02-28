package pingy.modules;

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class PingyJerseyModule extends JerseyServletModule {

    @Override
    protected void configureServlets() {
        bind(GuiceContainer.class);
        
        PackagesResourceConfig resourceConfig = new PackagesResourceConfig("pingy.resource");
        for (Class<?> resource : resourceConfig.getClasses()) {
            bind(resource);
        }
        
        serve("/*").with(GuiceContainer.class);
    }
    
}
