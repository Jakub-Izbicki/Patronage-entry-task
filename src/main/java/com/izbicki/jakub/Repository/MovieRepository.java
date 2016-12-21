package com.izbicki.jakub.Repository;

import com.izbicki.jakub.Entity.Movie;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface MovieRepository extends CrudRepository<Movie, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Movie SET title = :title WHERE id = :id")
    int updateMovieTitle(@Param("id") Long id, @Param("title") String name);

    @Modifying
    @Transactional
    @Query("UPDATE Movie SET description = :description WHERE id = :id")
    int updateMovieDesc(@Param("id") Long id, @Param("description") String description);
}
