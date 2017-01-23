package com.izbicki.jakub.Service;

import com.izbicki.jakub.Entity.Actor;
import com.izbicki.jakub.Entity.Cast;
import com.izbicki.jakub.Entity.Movie;
import com.izbicki.jakub.Error.ApiNotFoundException;
import com.izbicki.jakub.Repository.ActorRepository;
import com.izbicki.jakub.Repository.CastRepository;
import com.izbicki.jakub.Repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("CastService")
public class CastService {

    @Autowired
    private CastRepository castRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired @Qualifier("ActorService")
    private ActorService as;

    public ResponseEntity selectAll(){

        List<Cast> castsList = new ArrayList<>();

        for(Cast cast : castRepository.findAll()){

            castsList.add(cast);
        }
        return ResponseEntity.ok(castsList);
    }

    public ResponseEntity insert(Long movieId, Long actorId){

        Movie movie = movieRepository.findOne(movieId);
        Actor actor = actorRepository.findOne(actorId);

        if (movie == null)
            throw new ApiNotFoundException("movie");
        if (actor == null)
            throw new ApiNotFoundException("actor");

        Cast cast = new Cast(movie, actor);

        castRepository.save(cast);

        String location = "/admin/casts/" + cast.getId().toString();

        return ResponseEntity.status(HttpStatus.CREATED).header("Location", location).body(cast);
    }

    public ResponseEntity remove(Long movieId, Long actorId){

        Movie movie = movieRepository.findOne(movieId);
        Actor actor = actorRepository.findOne(actorId);

        if (movie == null)
            throw new ApiNotFoundException("movie");
        if (actor == null)
            throw new ApiNotFoundException("actor");

        List<Cast> castList = castRepository.selectCastWhereMovieActor(movie, actor);
            if (castList.size() > 0){

                Cast cast = castList.get(0);

                castRepository.delete(cast.getId());

                return ResponseEntity.ok(selectAll());
            }

            return ResponseEntity.ok(selectAll());
    }

    public ResponseEntity getActorsForMovie(Long movieId){

        Movie movie = movieRepository.findOne(movieId);

        if (movie == null)
            throw new ApiNotFoundException("movie");

        List<Cast> castList = castRepository.findCastByMovieId(movie);

        List<Actor> actors = new ArrayList<>();

        for(Cast cast : castList){
            actors.add(cast.getActor());
        }

        return ResponseEntity.ok(actors);
    }

    public ResponseEntity removeCastOfMovie(Long movieId){

        Movie movie = movieRepository.findOne(movieId);

        if (movie == null)
            throw new ApiNotFoundException("movie");

        castRepository.deleteCastOfMovie(movie);

        return selectAll();
    }
}
