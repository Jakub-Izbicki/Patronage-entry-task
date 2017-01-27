package com.izbicki.jakub.repository;

import com.izbicki.jakub.entity.Actor;
import com.izbicki.jakub.entity.Cast;
import com.izbicki.jakub.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface CastRepository extends JpaRepository<Cast, Long> {

    @Modifying
    @Transactional
    @Query("SELECT c FROM Cast c WHERE c.movie = :movie")
    List<Cast> findCastByMovieId(@Param("movie") Movie movie);

    @Modifying
    @Transactional
    @Query("DELETE FROM Cast c WHERE c.movie = :movie")
    void deleteCastOfMovie(@Param("movie") Movie movie);

    @Modifying
    @Transactional
    @Query("SELECT c FROM Cast c WHERE c.movie = :movie AND c.actor = :actor")
    List<Cast> selectCastWhereMovieActor(@Param("movie") Movie movie, @Param("actor") Actor actor);

}
