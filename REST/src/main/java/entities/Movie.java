package entities;

import org.codehaus.jackson.annotate.JsonBackReference;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@Entity
public class Movie implements Serializable{

    @Id
    private int movie_id;

    private String title;
    private String uri;


    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private MovieUser movie_user;

//    public MovieUser getMovie_user() {
//        return movie_user;
//    }

    public void setMovie_user(MovieUser movie_user) {
        this.movie_user = movie_user;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
