package endpoints;

import dao.MovieDao;
import entities.Movie;
import org.apache.http.HttpStatus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/movies")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class MovieEndpoint {
    private MovieDao movieDao=new MovieDao();

    @GET
    @Path("/title/{title}")
    //@Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByTitle(@PathParam("title") final String title){
        ArrayList<Movie> movies = (ArrayList<Movie>) movieDao.getAllMovies();
        List<Movie> filteredMovies = movies.stream()
                .filter(g -> g.getTitle().equals(title))
                .collect(Collectors.toList());
        if (!filteredMovies.isEmpty()){
            return Response.ok(filteredMovies.get(0)).build();
        }else{
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(){
       ArrayList<Movie> movies = (ArrayList<Movie>) movieDao.getAllMovies();
        if (!movies.isEmpty()) {
            return Response.ok(movies).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") final int id){
        try {
            Movie movie = movieDao.getMovieById(id);
            if (movie == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(movie).build();
        }catch (Exception e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    //content negotiation
    @GET
    @Consumes("text/uri-list")
    @Produces("text/plain")
    public Response movieUris() {
        List<Movie> movies=movieDao.getAllMovies();
        List<String> films = movies.stream()
                .map(Movie::getUri)
                .collect(Collectors.toList());
        return Response.ok(films).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(Movie movie) throws IllegalAccessException{
        try {
            movieDao.addMovie(movie);
            return Response.ok("Movie created: " + movie.getTitle() + " id: " + movie.getMovie_id()).build();
        } catch (Exception e) {
            return Response.status(HttpStatus.SC_NOT_FOUND).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAll(List<Movie> movies){
        try {
            movieDao.deleteAll();
            for (Movie m : movies) {
                movieDao.addMovie(m);
            }
            return Response.ok("Movies updated").build();
        }catch (Exception e){
            return Response.status(HttpStatus.SC_NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") final int id, Movie movie) {
        try {
            Movie byId = movieDao.getMovieById(id);
            if (byId == null) {
                movieDao.addMovie(movie);
                return Response.ok("Movie created: " + movie.getTitle() + " id: " + movie.getMovie_id()).build();
            } else {
                movieDao.updateMovieById(movie);
                return Response.ok("Movie of id: " + movie.getMovie_id() + " updated.").build();
            }
        }catch (Exception e){
            return Response.status(HttpStatus.SC_NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}/uri")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUri(@PathParam("id") final int id, String uri){
        try{
            movieDao.updateUri(id, uri);
            return Response.ok("Uri updated. Movie id: "+id+", new uri: "+uri).build();
        }catch (Exception e){
            return Response.status(HttpStatus.SC_NOT_FOUND).build();
        }
    }


    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") final int id){
        try{
            movieDao.deleteById(id);
            return Response.ok("Movie deleted").build();
        }catch(Exception e){
            return Response.status(HttpStatus.SC_NOT_FOUND).build();
        }
    }
}
