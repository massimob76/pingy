package pingy;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import pingy.modules.PingModule;
import pingy.modules.PingyJerseyModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;

public class PingServer {

    private static final String DEFAULT_PORT = "8000";
    
    public void start(int serverPort) throws Exception {
        Server server = new Server(serverPort);
        ServletContextHandler sch = new ServletContextHandler(server, "/");
        sch.addFilter(GuiceFilter.class, "/*", null);
        sch.addServlet(DefaultServlet.class, "/");
        
        server.start();
        server.join();
    }

    public static final void main(String[] args) throws Exception {
        Injector injector = Guice.createInjector(new PingModule(), new PingyJerseyModule());

        int serverPort = Integer.parseInt(System.getProperty("server.port", DEFAULT_PORT));
        PingServer pingServer = injector.getInstance(PingServer.class);
        pingServer.start(serverPort);
    }
}
