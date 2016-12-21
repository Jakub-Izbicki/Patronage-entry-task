package com.izbicki.jakub.Repository;

import com.izbicki.jakub.Entity.Actor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface ActorRepository  extends CrudRepository<Actor, Long>{

    @Modifying
    @Transactional
    @Query("UPDATE Actor SET name = :name WHERE id = :id")
    int updateActorName(@Param("id") Long id, @Param("name") String name);

}
