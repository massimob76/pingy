package pingy.resource;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;

@Path("/")
public class StaticAssetResource {

    private final String indexHtml;

    public StaticAssetResource() throws IOException {
        this.indexHtml = IOUtils.toString(this.getClass().getResourceAsStream("/web/ping.html"));
    }
    
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getIndexPage() {
        return indexHtml;
    }
    
}
