package dao;

import entities.Movie;
import entities.MovieUser;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class MovieDao {
    private EntityManagerFactory factory = Persistence.createEntityManagerFactory("NewPersistenceUnit");
    private EntityManager em = factory.createEntityManager();
    private String uri="http://localhost:8080/rest/movies/";

    public Movie getMovieById (int id){
        String hql = "FROM Movie m WHERE m.movie_id="+id;
        Query query = em.createQuery(hql);
        List<Movie> results =query.getResultList();
        if(results.isEmpty()){
            return null;
        }
        return results.get(0);
    }

    public List<Movie> getAllMovies(){
        String hql = "FROM Movie m";
        Query query = em.createQuery(hql);
        List<Movie> results =query.getResultList();
        return results;
    }

    public void addMovie(Movie movie){
        try{
            em.getTransaction().begin();
            movie.setUri(uri+movie.getMovie_id());
            em.persist(movie);
            em.getTransaction().commit();
        }catch(Exception e){
            System.err.println("Blad przy dodawaniu rekordu: " + e);
            throw e;
        }

    }

    public void updateMovieById(Movie movie){
        try{
            em.getTransaction().begin();
            em.merge(movie);
            em.getTransaction().commit();
        }catch (Exception e){
            System.err.println("Blad przy aktualizowaniu rekordu: "+ e);
            throw e;
        }
    }

    public void updateUri(int id, String link){
        try{
            em.getTransaction().begin();
            link=link.replaceAll("\"","\'");
            String hql = "update Movie m set m.uri="+link+" where m.movie_id="+id;
            Query query = em.createQuery(hql);
            query.executeUpdate();
            em.getTransaction().commit();
        }catch(Exception e){
            System.err.println("Błąd przy aktualizowaniu rekordu: "+e);
            throw  e;
        }
    }

    public void deleteAll(){
        try {
            em.getTransaction().begin();
            String hql = "DELETE from Movie m";
            Query query = em.createQuery(hql);
            query.executeUpdate();
            em.getTransaction().commit();
        }catch (Exception e){
            System.err.println("Błąd przy usuwaniu rekordów: " + e);
            throw e;
        }
    }

    public void deleteById(int id){
        try{
            em.getTransaction().begin();
            String hql = "DELETE from Movie m where m.movie_id="+id;
            Query query = em.createQuery(hql);
            query.executeUpdate();
            em.getTransaction().commit();
        }catch(Exception e){
            System.err.println("Błąd przy usuwaniu rekordu: "+e);
            throw e;
        }
    }
}
