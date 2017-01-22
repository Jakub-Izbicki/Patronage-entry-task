package com.izbicki.jakub.Controller;

import com.izbicki.jakub.Entity.Actor;
import com.izbicki.jakub.Service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class ActorController {

    @Autowired @Qualifier("ActorService")
    private ActorService as;

    @RequestMapping(value = "/actors", method = GET)
    public ResponseEntity selectAllActors(){

        return as.selectAll();
    }

    @RequestMapping(value = "/actors/{id}", method = GET)
    public ResponseEntity selectActorWhereId(@PathVariable("id") long id){

        return as.select(id);
    }

    @RequestMapping(value = "/admin/actors", method = POST)
    public ResponseEntity insertActor(@RequestParam(value = "name") String name  ){

        return as.insert(name);
    }

    @RequestMapping(value = "/admin/actors/{id}", method = DELETE)
    public ResponseEntity removeActorWhereId(@PathVariable("id") long id){

        return as.remove(id);
    }

    @RequestMapping(value = "/admin/actors/{id}", method = PUT)
    public ResponseEntity updateActorWhereId(@PathVariable("id") long id,
                                    @RequestParam(value="name") String name){

        return as.update(id, name);
    }
}
