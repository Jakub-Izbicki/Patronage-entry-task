package com.izbicki.jakub.Repository;

import com.izbicki.jakub.Entity.Movie;
import com.izbicki.jakub.Entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface MovieRepository extends CrudRepository<Movie, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Movie SET title = :title WHERE id = :id")
    int updateMovieTitle(@Param("id") Long id, @Param("title") String name);

    @Modifying
    @Transactional
    @Query("UPDATE Movie SET description = :description WHERE id = :id")
    int updateMovieDesc(@Param("id") Long id, @Param("description") String description);

    @Modifying
    @Transactional
    @Query("UPDATE Movie SET type = :type WHERE id = :id")
    int updateMovieType(@Param("id") Long id, @Param("type") int type);

    @Modifying
    @Transactional
    @Query("UPDATE Movie SET price = :price WHERE id = :id")
    int updateMoviePrice(@Param("id") Long id, @Param("price") float price);


    @Modifying
    @Transactional
    @Query("SELECT m FROM Movie m WHERE m.type = 0 AND m.isAvailable = true")
    List<Movie> selectAvailableNewest();

    @Modifying
    @Transactional
    @Query("SELECT m FROM Movie m WHERE m.type = 1 AND m.isAvailable = true")
    List<Movie> selectAvailableHits();

    @Modifying
    @Transactional
    @Query("SELECT m FROM Movie m WHERE m.type = 2 AND m.isAvailable = true")
    List<Movie> selectAvailableOther();

    @Modifying
    @Transactional
    @Query("SELECT m FROM Movie m WHERE m.isAvailable = true")
    List<Movie> selectAvailable();

    @Modifying
    @Transactional
    @Query("UPDATE Movie SET isAvailable = false, rentedBy = :user WHERE id IN :idList")
    void rentMovies(@Param("user") User user, @Param("idList") List<Long> idList);

    @Modifying
    @Transactional
    @Query("SELECT m FROM Movie m WHERE m.rentedBy = :user AND m.isAvailable = false")
    List<Movie> selectRentedMovies(@Param("user") User user);

    @Modifying
    @Transactional
    @Query("UPDATE Movie SET isAvailable = true WHERE id IN :idList")
    void returnMovies(@Param("idList") List<Long> idList);

    @Modifying
    @Transactional
    @Query("SELECT m FROM Movie m WHERE m.id IN :idList")
    List<Movie> getMoviesByIds(@Param("idList") List<Long> idList);
}
