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


@ManagedBean(name = "users")
@ViewScoped
public class UsersView {

    private LinkedList<String> users;
    private LinkedList<String> movies;
    private int user_id;
    private String ret_user;
    private String name;
    private String avatar;
    private int age;
    private String message;
    private int movie_id;
    private String title;

    public void restGetUsers() throws Exception{
        ResteasyClient client = new ResteasyClientBuilder().build();
        Response response = client.target("http://localhost:8080/rest/users")
                .request("application/json").get();
        int status = response.getStatus();
        System.out.println("Status code: " + status);
        String jsonData = response.readEntity(String.class);
        LinkedList<String> ret=new LinkedList<>();
        JSONArray json=new JSONArray(jsonData);
        for(Object o: json){
            JSONObject jsonObject=(JSONObject) o;
            String str="";
            str+="Id: "+jsonObject.get("user_id").toString()+"; ";
            str+="Name: "+jsonObject.get("name").toString()+"; ";
            str+="Age: "+jsonObject.get("age").toString();
            ret.add(str);
        }
        this.users=ret;
    }

    public void restGetUser() throws Exception{
        ResteasyClient client = new ResteasyClientBuilder().build();
        Response response = client.target("http://localhost:8080/rest/users/"+user_id)
                .request("application/json").get();
        int status = response.getStatus();
        System.out.println("Status code: " + status);
        if(status==200){
            String jsonData = response.readEntity(String.class);
            JSONObject jsonObject=new JSONObject(jsonData);
            ret_user="";
            ret_user+="Id: "+jsonObject.get("user_id").toString()+"; ";
            ret_user+="Name: "+jsonObject.get("name").toString()+"; ";
            ret_user+="Age: "+jsonObject.get("age").toString();

        }else{
            ret_user="Nie znaleziono użytkownika";
        }
    }

    public void restGetUserMovies() throws Exception{
        ResteasyClient client = new ResteasyClientBuilder().build();
        Response response = client.target("http://localhost:8080/rest/users/"+user_id+"/movies")
                .request("application/json").get();
        int status = response.getStatus();
        System.out.println("Status code: " + status);
        String jsonData = response.readEntity(String.class);
        LinkedList<String> ret=new LinkedList<>();
        JSONArray json=new JSONArray(jsonData);
        for(Object o:json){
            JSONObject jsonObject=(JSONObject) o;
            String str="";
            str+="Id: "+jsonObject.get("movie_id").toString()+"; ";
            str+="Title: "+jsonObject.get("title").toString()+"; ";
            str+="URI: "+jsonObject.get("uri").toString();
            ret.add(str);
        }
        this.movies=ret;
    }

    public void restAddUser(){
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://localhost:8080/rest/users");
        Response response = target.request()
                .post(Entity.entity("{\"user_id\":"+user_id+",\"name\":\""+name+"\", " +
                        "\"age\":"+age+",\"avatar\":\""+avatar+"\"}", MediaType.APPLICATION_JSON));
        int status = response.getStatus();

        System.out.println("Status code: " + status);
        if(status==200){
            message=response.readEntity(String.class);
        }else{
            message="Nie udało się dodać filmu";
        }
    }

    public void restAddMovie(){
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://localhost:8080/rest/users/"+user_id+"/movies");
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

    public void restDeleteUser(){
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://localhost:8080/rest/users/"+user_id);
        Response response = target.request()
                .delete();
        int status = response.getStatus();

        System.out.println("Status code: " + status);
        if(status==200){
            message=response.readEntity(String.class);
        }else{
            message="Nie udało się usunąć użytkownika";
        }
    }

    public LinkedList<String> getUsers() {
        return users;
    }

    public void setUsers(LinkedList<String> users) {
        this.users = users;
    }

    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getRet_user() {
        return ret_user;
    }

    public void setRet_user(String ret_user) {
        this.ret_user = ret_user;
    }

    public LinkedList<String> getMovies() {
        return movies;
    }

    public void setMovies(LinkedList<String> movies) {
        this.movies = movies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
