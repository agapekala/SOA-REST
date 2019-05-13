package dao;

import entities.Movie;
import entities.MovieUser;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;
import java.util.Set;

public class UserDao {

    private EntityManagerFactory factory = Persistence.createEntityManagerFactory("NewPersistenceUnit");
    private EntityManager em = factory.createEntityManager();
    private String uri="http://localhost:8080/rest/movies/";

    public List<MovieUser> getAllUsers(){
        String hql = "FROM MovieUser m";
        Query query = em.createQuery(hql);
        List<MovieUser> results =query.getResultList();
        return results;
    }

    public MovieUser getUserById(int id){
        String hql = "FROM MovieUser m WHERE m.user_id="+id;
        Query query = em.createQuery(hql);
        List<MovieUser> results =query.getResultList();
        if(results.isEmpty()){
            return null;
        }
        return results.get(0);
    }

    public List<Movie> getUserMovies(int id){
        String hql = "SELECT movies FROM MovieUser m WHERE m.user_id="+id;
        Query query = em.createQuery(hql);
        List<Movie> results =query.getResultList();
        if(results.isEmpty()){
            return null;
        }
        return results;
    }

    public void addUser(MovieUser user){
        try{

            em.getTransaction().begin();
            Set<Movie> movies=user.getMovies();
            if(movies!=null){
                for(Movie m:movies) {
                    m.setMovie_user(user);
                    em.persist(m);
                }
            }
            em.persist(user);
            em.getTransaction().commit();
        }catch(Exception e){
            System.err.println("Blad przy dodawaniu rekordu: " + e);
        }

    }

    public void addMovie(int id, Movie m){
        try{
            MovieUser u=getUserById(id);
            em.getTransaction().begin();
            m.setMovie_user(u);
            m.setUri(uri+m.getMovie_id());
            em.persist(m);
            em.getTransaction().commit();
        }catch(Exception e){
            System.err.println("Blad przy dodawaniu rekordu: " + e);
            throw e;
        }
    }

    public void updateUserById(MovieUser user){
        try{

            em.getTransaction().begin();
            String hql1 ="update MovieUser m set m.age="+user.getAge() +
                    ", m.name=\'"+user.getName()+"\', m.avatar=\'"+user.getAvatar() +
                    "\' where m.user_id="+user.getUser_id();
            Query q = em.createQuery(hql1);
            q.executeUpdate();
            em.getTransaction().commit();
        }catch (Exception e){
            System.err.println("Blad przy aktualizowaniu rekordu: "+ e);
            throw e;
        }
    }

//    public void updateMovie(int id, Movie movie){
//        try{
//            em.getTransaction().begin();
//            String hql="";
//            Query q=em.createQuery(hql);
//            q.executeUpdate();
//            em.getTransaction().commit();
//        }catch (Exception e){
//            System.err.println("Blad przy aktualizowaniu rekordu: "+ e);
//            throw e;
//        }
//    }

    public void deleteAll(){
        try {
            em.getTransaction().begin();
            String hql1 ="DELETE from Movie m where m.movie_user is not null";
            Query q = em.createQuery(hql1);
            q.executeUpdate();
            String hql = "DELETE from MovieUser m";
            Query query = em.createQuery(hql);
            query.executeUpdate();
            em.getTransaction().commit();
        }catch (Exception e){
            System.err.println("Błąd przy usuwaniu rekordów: " + e);
            throw e;
        }
    }

    public void deleteAllMovies(int id){
        try {
            em.getTransaction().begin();
            String hql1 = "DELETE from Movie m where m.movie_user=" + id;
            Query q = em.createQuery(hql1);
            q.executeUpdate();
            em.getTransaction().commit();
        }catch (Exception e){
            System.err.println("Błąd przy usuwaniu rekordów: " + e);
            throw e;
        }

    }

    public void deleteById(int id){
        try{
            em.getTransaction().begin();
            String hql1="DELETE from Movie m where m.movie_user="+id;
            Query q=em.createQuery(hql1);
            q.executeUpdate();
            String hql = "DELETE from MovieUser m where m.user_id="+id;
            Query query = em.createQuery(hql);
            query.executeUpdate();
            em.getTransaction().commit();
        }catch(Exception e){
            System.err.println("Błąd przy usuwaniu rekordu: "+e);
            throw e;
        }
    }

    public void deleteMovie(int id, int movie){
        try{
            em.getTransaction().begin();
            String hql="DELETE FROM Movie m WHERE m.movie_user="+id+" and m.movie_id="+movie;
            Query q=em.createQuery(hql);
            q.executeUpdate();
            em.getTransaction().commit();
        }catch(Exception e){
            System.err.println("Błąd przy usuwaniu rekordu: "+e);
            throw e;
        }
    }
}
