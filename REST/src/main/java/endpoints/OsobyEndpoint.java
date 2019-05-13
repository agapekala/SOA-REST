package endpoints;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/osoby")
public class OsobyEndpoint {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response redirect(){
        return Response.temporaryRedirect(java.net.URI.create((("http://localhost:8080/rest/users")))).build();
    }
}
