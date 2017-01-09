package com.izbicki.jakub.Controller;

import com.izbicki.jakub.Entity.Actor;
import com.izbicki.jakub.Entity.Cast;
import com.izbicki.jakub.Entity.Movie;
import com.izbicki.jakub.MovieType;
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
public class MovieController {

    @Autowired @Qualifier("MovieService")
    private MovieService ms;

    @Autowired @Qualifier("CastService")
    private CastService cs;

    @RequestMapping(value = "admin/movies", method = GET)
    public List<Movie> selectAllMovies(){

        return ms.selectAll();
    }

    @RequestMapping(value = "/movies/{id}", method = GET)
    public Movie selectMovieWhereId(@PathVariable("id") long id){

        return ms.selectMovie(id);
    }

    @RequestMapping(value = "admin/movies/insert", method = PUT)
    public Movie insertMovie(@RequestParam(value = "title") String title,
                             @RequestParam(value = "desc") String desc,
                             @RequestParam(value = "type") int type,
                             @RequestParam(value = "price") float price){

        if ((type - 2) > 0)
            throw new IllegalArgumentException();

        MovieType movieType = MovieType.values()[type];

        return ms.insert(title, desc, movieType, price);
    }

    @RequestMapping(value = "admin/movies/remove/{id}", method = DELETE)
    public List<Movie> removeMovieWhereId(@PathVariable("id") Long id){

        return ms.remove(id);
    }

    @RequestMapping(value = "admin/movies/update/{id}", method = POST)
    public Movie getActorsForMovie(@PathVariable("id") int id,
                                   @RequestParam(value="title", required = false) String title,
                                   @RequestParam(value="desc", required = false) String desc){

        return ms.update(id, title, desc);
    }

    @RequestMapping(value = "/movies/{id}/actors", method = GET)
    public List<Actor> getActorsForMovie(@PathVariable("id") Long id){

        return cs.getActorsForMovie(id);
    }

    @RequestMapping(value = "admin/movies/{id}/addActor/{actorId}", method = PUT)
    private Cast insertCast(@PathVariable(value = "id")Long movieId,
                            @PathVariable(value = "actorId")Long actorId){

        return cs.insert(movieId, actorId);
    }

    @RequestMapping(value = "admin/movies/{id}/removeActor/{actorId}", method = DELETE)
    private List<Cast> removeCast(@PathVariable(value = "id") Long movieId,
                                  @PathVariable(value = "actorId")Long actorId){

        return cs.remove(movieId, actorId);
    }

    @RequestMapping(value = "/movies/newest", method = GET)
    private List<Movie> selectNewest(){

        return ms.selectNewest();
    }

    @RequestMapping(value = "/movies/hits", method = GET)
    private List<Movie> selectHits(){

        return ms.selectHits();
    }

    @RequestMapping(value = "/movies/other", method = GET)
    private List<Movie> selectOther(){

        return ms.selectOther();
    }

    @RequestMapping(value = "/movies/available", method = GET)
    private List<Movie> selectAvaliable(){

        return ms.selectAvaliable();
    }
}