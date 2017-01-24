package com.izbicki.jakub.service;

import com.izbicki.jakub.entity.Actor;
import com.izbicki.jakub.error.ApiNotFoundException;
import com.izbicki.jakub.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("ActorService")
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;

    public ResponseEntity selectAll(){

        List<Actor> actorList = new ArrayList<>();

        for(Actor actor : actorRepository.findAll()){

            actorList.add(actor);
        }
        return ResponseEntity.ok(actorList);
    }

    public ResponseEntity select(long id){

        Actor actor = actorRepository.findOne(id);

        if (actor == null)
            throw new ApiNotFoundException("actor");

        return ResponseEntity.ok(actor);
    }

    public ResponseEntity insert(String name){

        Actor actor = new Actor(name);

        actorRepository.save(actor);

        String location = "/actors/" + actor.getId().toString();

        return ResponseEntity.status(HttpStatus.CREATED).header("Location", location).body(actor);
    }

    public ResponseEntity remove(long id){

        if (actorRepository.findOne(id) == null)
            throw new ApiNotFoundException("actor");

        actorRepository.delete(id);
        return selectAll();
    }

    public ResponseEntity update(long id, String name){

        Actor actor = actorRepository.findOne(id);

        if (actor == null)
            throw new ApiNotFoundException("actor");

        actorRepository.updateActorName(id, name);

        actor.setName(name);

        return ResponseEntity.ok(actor);
    }
}
