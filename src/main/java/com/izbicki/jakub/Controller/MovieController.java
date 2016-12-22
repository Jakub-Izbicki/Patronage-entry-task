package com.izbicki.jakub.Controller;

import com.izbicki.jakub.Entity.Actor;
import com.izbicki.jakub.Entity.Cast;
import com.izbicki.jakub.Entity.Movie;
import com.izbicki.jakub.Service.CastService;
import com.izbicki.jakub.Service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/movies", produces="application/json")
public class MovieController {

    @Autowired @Qualifier("MovieService")
    private MovieService ms;

    @Autowired @Qualifier("CastService")
    private CastService cs;

    @RequestMapping(value = "", method = GET)
    public List<Movie> selectAllMovies(){

        return ms.selectAll();
    }

    @RequestMapping(value = "/{id}", method = GET)
    public Movie selectMovieWhereId(@PathVariable("id") long id){

        return ms.selectMovie(id);
    }

    @RequestMapping(value = "/insert", method = PUT)
    public Movie insertMovie(@RequestParam(value = "title") String title,
                             @RequestParam(value = "desc") String desc){

        return ms.insert(title, desc);
    }

    @RequestMapping(value = "/remove/{id}", method = DELETE)
    public List<Movie> removeMovieWhereId(@PathVariable("id") Long id){

        return ms.remove(id);
    }

    @RequestMapping(value = "/update/{id}", method = POST)
    public Movie updateMovieWhereId(@PathVariable("id") int id,
                                   @RequestParam(value="title", required = false) String title,
                                   @RequestParam(value="desc", required = false) String desc){

        return ms.update(id, title, desc);
    }

    @RequestMapping(value = "/{id}/actors", method = GET)
    public List<Actor> updateMovieWhereId(@PathVariable("id") Long id){

        return cs.getActorsForMovie(id);
    }

    @RequestMapping(value = "/{id}/addActor/{actorId}", method = PUT)
    private Cast insertCast(@PathVariable(value = "id")Long movieId,
                            @PathVariable(value = "actorId")Long actorId){

        return cs.insert(movieId, actorId);
    }

    @RequestMapping(value = "/{id}/removeActor/{actorId}", method = DELETE)
    private List<Cast> removeCast(@PathVariable(value = "id") Long movieId,
                                  @PathVariable(value = "actorId")Long actorId){

        return cs.remove(movieId, actorId);
    }
}
