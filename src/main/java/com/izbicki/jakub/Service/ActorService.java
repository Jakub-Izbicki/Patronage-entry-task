package com.izbicki.jakub.Service;

import com.izbicki.jakub.Entity.Actor;
import com.izbicki.jakub.Repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakub on 19.12.2016.
 */

@Component("ActorService")
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;

    public List<Actor> selectAll(){

        List<Actor> actorList = new ArrayList<>();

        for(Actor actor : actorRepository.findAll()){

            actorList.add(actor);
        }
        return actorList;
    }

    public Actor select(long id){

        return actorRepository.findOne(id);
    }

    public Actor insert(String name){

        Actor actor = new Actor(name);

        actorRepository.save(actor);

        return actor;
    }

    public List<Actor> remove(long id){

        actorRepository.delete(id);
        return selectAll();
    }

    public Actor update(long id, String name){

        Actor actor = actorRepository.findOne(id);

        actorRepository.updateActorName(id, name);

        actor.setName(name);

        return actor;
    }
}
