package entities;


import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Set;

@Entity
public class MovieUser implements Serializable {
    @Id
    private int user_id;

    private String name;
    private int age;
    private String avatar;

    @JsonManagedReference
    @OneToMany(fetch=FetchType.EAGER,mappedBy = "movie_user", orphanRemoval = true)
    private Set<Movie> movies;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }
}
