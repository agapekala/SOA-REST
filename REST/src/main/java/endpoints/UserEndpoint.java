package endpoints;

import dao.MovieDao;
import dao.UserDao;
import entities.Movie;
import entities.MovieUser;
import org.apache.http.HttpStatus;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/users")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class UserEndpoint {

    private UserDao userDao=new UserDao();
    private MovieDao movieDao=new MovieDao();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(){
        ArrayList<MovieUser> users = (ArrayList<MovieUser>) userDao.getAllUsers();
        if (!users.isEmpty()) {
            return Response.ok(users).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") final int id){
        try {
            MovieUser user=userDao.getUserById(id);
            if(user==null){
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(user).build();
        }catch(Exception e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Consumes("text/uri-list")
    @Produces("text/plain")
    public Response allNames() {
        List<MovieUser> users=userDao.getAllUsers();
        List<String> names = users.stream()
                .map(MovieUser::getName)
                .collect(Collectors.toList());
        return Response.ok(names).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/movies")
    public Response getUserMovies(@PathParam("id") final int id){
        try{
            List<Movie> filteredMovies=userDao.getUserMovies(id);
            return Response.ok(filteredMovies).build();
        }catch(Exception e){
            return Response.status(HttpStatus.SC_NOT_FOUND).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/movies/{mov_id}")
    public Response getUserMovie(@PathParam("id") final int id, @PathParam("mov_id") final int mov_id){
        try{
            List<Movie> movies=userDao.getUserMovies(id);
            List<Movie> movie=movies.stream()
                    .filter(g->g.getMovie_id()==mov_id)
                    .collect(Collectors.toList());

            return  Response.ok(movie.get(0)).build();

        }catch (Exception e){
            return Response.status(HttpStatus.SC_NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}/avatar")
    @Produces("image/png")
    public Response getAvatar(@PathParam("id") final int id){
        try{
            String path=userDao.getUserById(id).getAvatar();
            File file = new File(path);
            Response.ResponseBuilder response = Response.ok((Object) file);
            response.header("Content-Disposition",
                    "attachment; filename=avatar.png");
            return response.build();
        }catch (Exception e){
            return Response.status(HttpStatus.SC_NOT_FOUND).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveUser(MovieUser user) throws IllegalAccessException{
        try {
            userDao.addUser(user);
            return Response.ok("User created: " + user.getName() + " id: " + user.getUser_id()
            + " age: "+user.getAge()).build();
        } catch (Exception e) {
            return Response.status(HttpStatus.SC_NOT_FOUND).build();
        }
    }

    @POST
    @Path("/{id}/movies")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveUserMovie(@PathParam("id") final int id, Movie movie) {
        try{
            userDao.addMovie(id,movie);
            return Response.ok("Movie with id="+movie.getMovie_id()+" added for user "+id).build();
        }catch(Exception e){
            return Response.status(HttpStatus.SC_NOT_FOUND).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAll(List<MovieUser> movies){
        try {
            userDao.deleteAll();
            for (MovieUser m : movies) {
                userDao.addUser(m);
            }
            return Response.ok("Users updated").build();
        }catch (Exception e){
            return Response.status(HttpStatus.SC_NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") final int id, MovieUser user) {
        try {
            MovieUser byId = userDao.getUserById(id);
            if (byId == null) {
                userDao.addUser(user);
                return Response.ok("User created: " + user.getName() + " id: " + user.getUser_id()).build();
            } else {
                userDao.updateUserById(user);
                return Response.ok("User: " + user.getUser_id() + " updated.").build();
            }
        }catch (Exception e){
            return Response.status(HttpStatus.SC_NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}/movies")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMovies(@PathParam("id") final int id, List<Movie> movies) {
        try {
            userDao.deleteAllMovies(id);
            for (Movie m : movies) {
                userDao.addMovie(id,m);
            }
            return Response.ok("Movies updated for user id: "+id).build();
        }catch (Exception e){
            return Response.status(HttpStatus.SC_NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}/movies/{mov_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMovie(@PathParam("id") final int id, @PathParam("mov_id") final int mov_id
    ,Movie movie){
        try{
            List<Movie> movies=userDao.getUserMovies(id);
            List<Movie> filteredMovies=movies.stream()
                    .filter(g -> g.getMovie_id()==mov_id)
                    .collect(Collectors.toList());
            if(filteredMovies.isEmpty()){
                userDao.addMovie(id,movie);
                return Response.ok("Movie created: " + movie.getMovie_id()).build();
            }else{
                MovieUser user=userDao.getUserById(id);
                movie.setMovie_user(user);
                movieDao.updateMovieById(movie);
                return Response.ok("Movie: " + movie.getMovie_id() + " updated.").build();
            }
        }catch (Exception e){
            return Response.status(HttpStatus.SC_NOT_FOUND).build();
        }
    }



    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") final int id){
        try{
            userDao.deleteById(id);
            return Response.ok("User deleted").build();
        }catch(Exception e){
            return Response.status(HttpStatus.SC_NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}/movies/{mov_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteMovie(@PathParam("id") final int id, @PathParam("mov_id") final int mov_id){
        try{
            userDao.deleteMovie(id, mov_id);
            return Response.ok("Movie with id="+mov_id+" deleted.").build();
        }catch (Exception e){
            return Response.status(HttpStatus.SC_NOT_FOUND).build();
        }
    }
}
