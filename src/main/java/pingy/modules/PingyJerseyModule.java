package pingy.modules;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import com.google.inject.Scopes;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class PingyJerseyModule extends JerseyServletModule {

    @Override
    protected void configureServlets() {
        bind(GuiceContainer.class);
        bind(JacksonJsonProvider.class).in(Scopes.SINGLETON);
        
        PackagesResourceConfig resourceConfig = new PackagesResourceConfig("pingy.resource");
        for (Class<?> resource : resourceConfig.getClasses()) {
            bind(resource);
        }
        
        serve("/*").with(GuiceContainer.class);
    }
    
}
