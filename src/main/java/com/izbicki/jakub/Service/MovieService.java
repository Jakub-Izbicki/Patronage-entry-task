package com.izbicki.jakub.Service;

import com.izbicki.jakub.Entity.Movie;
import com.izbicki.jakub.MovieType;
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

    public Movie insert(String title, String desc, MovieType type, float price){

        Movie movie = new Movie(title, desc, type, price, true);

        movieRepository.save(movie);
        return movie;
    }

    public List<Movie> remove(long id){

        cs.removeCastOfMovie(id);

        movieRepository.delete(id);

        return selectAll();
    }

    public Movie update(long id, String title, String desc, Integer type, Float price){

        if (title != null)
            movieRepository.updateMovieTitle(id, title);
        if (desc != null)
            movieRepository.updateMovieDesc(id, desc);
        if (type != null)
            movieRepository.updateMovieType(id, type);
        if (price != null)
            movieRepository.updateMoviePrice(id, price);

        return movieRepository.findOne(id);
    }

    public List<Movie> selectNewest(){

        List<Movie> moviesList = new ArrayList<>();

        for(Movie movie : movieRepository.selectAvailableNewest())
            moviesList.add(movie);

        return moviesList;
    }

    public List<Movie> selectHits(){

        List<Movie> moviesList = new ArrayList<>();

        for(Movie movie : movieRepository.selectAvailableHits())
            moviesList.add(movie);

        return moviesList;
    }

    public List<Movie> selectOther(){

        List<Movie> moviesList = new ArrayList<>();

        for(Movie movie : movieRepository.selectAvailableOther())
            moviesList.add(movie);

        return moviesList;
    }

    public List<Movie> selectAvaliable(){

        List<Movie> moviesList = new ArrayList<>();

        for(Movie movie : movieRepository.selectAvailable())
            moviesList.add(movie);

        return moviesList;
    }
}
