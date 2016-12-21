package com.izbicki.jakub.Service;

import com.izbicki.jakub.Entity.Movie;
import com.izbicki.jakub.Repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("MovieService")
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired @Qualifier("CastService")
    private CastService cs;

    public List<Movie> selectAll(){

        List<Movie> moviesList = new ArrayList<>();

        for(Movie movie : movieRepository.findAll()){

            moviesList.add(movie);
        }
        return moviesList;
    }

    public Movie selectMovie(long id){

        return movieRepository.findOne(id);
    }

    public Movie insert(String title, String desc){

        Movie movie = new Movie(title, desc);

        movieRepository.save(movie);
        return movie;
    }

    public List<Movie> remove(long id){

        cs.removeCastOfMovie(id);

        movieRepository.delete(id);

        return selectAll();
    }

    public Movie update(long id, String title, String desc){

        if (title != null)
            movieRepository.updateMovieTitle(id, title);
        if (desc != null)
            movieRepository.updateMovieDesc(id, desc);

        return movieRepository.findOne(id);
    }
}
