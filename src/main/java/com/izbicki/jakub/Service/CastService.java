package com.izbicki.jakub.Service;

import com.izbicki.jakub.Entity.Actor;
import com.izbicki.jakub.Entity.Cast;
import com.izbicki.jakub.Entity.Movie;
import com.izbicki.jakub.Repository.ActorRepository;
import com.izbicki.jakub.Repository.CastRepository;
import com.izbicki.jakub.Repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    public List<Cast> selectAll(){

        List<Cast> castsList = new ArrayList<>();

        for(Cast cast : castRepository.findAll()){

            castsList.add(cast);
        }
        return castsList;
    }

    public Cast insert(Long movieId, Long actorId){

        Cast cast = new Cast(movieRepository.findOne(movieId), actorRepository.findOne(actorId));

        castRepository.save(cast);

        return cast;
    }

    public List<Cast> remove(Long movieId, Long actorId){

        List<Cast> castList = castRepository.selectCastWhereMovieActor(movieRepository.findOne(movieId),
                                                             actorRepository.findOne(actorId));
            if (castList.size() > 0){

                Cast cast = castList.get(0);

                castRepository.delete(cast.getId());

                return selectAll();
            }

            return selectAll();
    }

    public List<Actor> getActorsForMovie(Long movieId){

        Movie movie = movieRepository.findOne(movieId);

        List<Cast> castList = castRepository.findCastByMovieId(movie);

        List<Actor> actors = new ArrayList<>();

        for(Cast cast : castList){
            actors.add(cast.getActor());
        }

        return actors;
    }

    public List<Cast> removeCastOfMovie(Long movieId){

        Movie movie = movieRepository.findOne(movieId);
        castRepository.deleteCastOfMovie(movie);

        return selectAll();
    }
}
