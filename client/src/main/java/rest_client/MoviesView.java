package rest_client;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;

@ManagedBean(name = "movies")
@ViewScoped
public class MoviesView {

    private LinkedList<String> movies;
    private int movie_id;
    private String ret_movie;
    private String title;
    private String uri;
    private String message;

    public void restGetMovies(){
        ResteasyClient client = new ResteasyClientBuilder().build();
        Response response = client.target("http://localhost:8080/rest/movies").request().get();
        int status = response.getStatus();
        System.out.println("Status code: " + status);
        String jsonData = response.readEntity(String.class);
        LinkedList<String> ret=new LinkedList<>();
        JSONArray json=new JSONArray(jsonData);
        for(Object o:json){
            JSONObject jsonObject=(JSONObject) o;
            String str="";
            str+="Id : "+jsonObject.get("movie_id")+"; ";
            str+="Title: "+jsonObject.get("title")+"; ";
            str+="URI: "+jsonObject.get("uri");
            ret.add(str);
        }
        this.movies=ret;
    }

    public void restGetMovie(){
        ResteasyClient client = new ResteasyClientBuilder().build();
        Response response = client.target("http://localhost:8080/rest/movies/"+movie_id)
                .request("application/json").get();
        int status = response.getStatus();
        System.out.println("Status code: " + status);
        if(status==200){
            String jsonData = response.readEntity(String.class);
            JSONObject jsonObject=new JSONObject(jsonData);
            ret_movie="";
            ret_movie+="Id: "+jsonObject.get("movie_id").toString()+"; ";
            ret_movie+="Title: "+jsonObject.get("title").toString()+"; ";
            ret_movie+="URI: "+jsonObject.get("uri").toString();

        }else{
            ret_movie="Nie znaleziono filmu";
        }
    }

    public void restGetMovieByTitle(){
        ResteasyClient client = new ResteasyClientBuilder().build();
        Response response = client.target("http://localhost:8080/rest/movies/title/"+title)
                .request("application/json").get();
        int status = response.getStatus();
        System.out.println("Status code: " + status);
        if(status==200){
            String jsonData = response.readEntity(String.class);
            JSONObject jsonObject=new JSONObject(jsonData);
            ret_movie="";
            ret_movie+="Id: "+jsonObject.get("movie_id").toString()+"; ";
            ret_movie+="Title: "+jsonObject.get("title").toString()+"; ";
            ret_movie+="URI: "+jsonObject.get("uri").toString();

        }else{
            ret_movie="Nie znaleziono filmu";
        }
    }

    public void addMovie(){
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://localhost:8080/rest/movies");
        Response response = target.request()
                .post(Entity.entity("{\"movie_id\":"+movie_id+",\"title\":\""+title+"\"}", MediaType.APPLICATION_JSON));
        int status = response.getStatus();

        System.out.println("Status code: " + status);
        if(status==200){
            message=response.readEntity(String.class);
        }else{
            message="Nie udało się dodać filmu";
        }
    }

    public void restDeleteMovie(){
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://localhost:8080/rest/movies/"+movie_id);
        Response response = target.request()
                .delete();
        int status = response.getStatus();

        System.out.println("Status code: " + status);
        if(status==200){
            message=response.readEntity(String.class);
        }else{
            message="Nie udało się usunąć filmu";
        }
    }

    public void restUpdateMovie(){
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://localhost:8080/rest/movies/"+movie_id);
        Response response = target.request()
                .put(Entity.entity("{\"movie_id\":"+movie_id+",\"title\":\""+title+"\",\"uri\":\""+uri+"\"}", MediaType.APPLICATION_JSON));
        int status = response.getStatus();

        System.out.println("Status code: " + status);
        if(status==200){
            message=response.readEntity(String.class);
        }else{
            message="Nie udało się edytować filmu";
        }
    }

    public LinkedList<String> getMovies() {
        return movies;
    }

    public void setMovies(LinkedList<String> movies) {
        this.movies = movies;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public String getRet_movie() {
        return ret_movie;
    }

    public void setRet_movie(String ret_movie) {
        this.ret_movie = ret_movie;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
