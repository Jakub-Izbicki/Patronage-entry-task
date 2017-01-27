package com.izbicki.jakub.service;

import com.izbicki.jakub.MovieType;
import com.izbicki.jakub.entity.Movie;
import com.izbicki.jakub.error.ApiCustomException;
import com.izbicki.jakub.error.ApiNotFoundException;
import com.izbicki.jakub.error.ErrorCodes;
import com.izbicki.jakub.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component("MovieService")
public class MovieService {

    @Autowired
    private ResourceBundleMessageSource exceptionMessageSource, applicationConfigMessageSource;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    @Qualifier("CastService")
    private CastService cs;

    public ResponseEntity selectAll() {

        List<Movie> moviesList = new ArrayList<>();

        for (Movie movie : movieRepository.findAll()) {

            moviesList.add(movie);
        }
        return ResponseEntity.ok(moviesList);
    }

    public ResponseEntity selectMovie(long id) {

        Movie movie = movieRepository.findOne(id);

        if (movie == null)
            throw new ApiNotFoundException("movie");

        return ResponseEntity.ok(movie);
    }

    public ResponseEntity insert(String title, String desc, int movieType, BigDecimal price) {


        if (!Arrays.asList(0, 1, 2).contains(movieType))
            throw new ApiCustomException(HttpStatus.BAD_REQUEST,
                    getExceptionMsgSource(ErrorCodes.BAD_MOVIE_ENUM),
                    getExceptionMsgSource(ErrorCodes.BAD_MOVIE_ENUM_USER));

        MovieType type = MovieType.values()[movieType];

        Movie movie = new Movie(title, desc, type, price, true);

        movieRepository.save(movie);

        String location = "/movies/" + movie.getId().toString();

        System.out.println(location);

        return ResponseEntity.status(HttpStatus.CREATED).header("Location", "asa").body(movie);
    }

    public ResponseEntity remove(long id) {

        cs.removeCastOfMovie(id);

        movieRepository.delete(id);

        return selectAll();
    }

    public ResponseEntity update(long id, String title, String desc, Integer type, Float price) {

        if (movieRepository.findOne(id) == null)
            throw new ApiNotFoundException("movie");

        if (title != null)
            movieRepository.updateMovieTitle(id, title);
        if (desc != null)
            movieRepository.updateMovieDesc(id, desc);
        if (type != null)
            movieRepository.updateMovieType(id, type);
        if (price != null)
            movieRepository.updateMoviePrice(id, price);

        return ResponseEntity.ok(movieRepository.findOne(id));
    }

    public ResponseEntity selectAvailable(Integer category, Integer page, Integer pageSize, String sortBy) {

        if (page == null ^ pageSize == null) {

            throw new ApiCustomException(HttpStatus.BAD_REQUEST,
                    getExceptionMsgSource(ErrorCodes.BAD_PAGE_PARAM),
                    getExceptionMsgSource(ErrorCodes.BAD_PAGE_PARAM_USER));
        }

        if (category != null && !Arrays.asList(0, 1, 2).contains(category)) {

            throw new ApiCustomException(HttpStatus.BAD_REQUEST,
                    getExceptionMsgSource(ErrorCodes.BAD_MOVIE_ENUM),
                    getExceptionMsgSource(ErrorCodes.BAD_MOVIE_ENUM_USER));
        }

        String sortByProperty = sortBy != null ? sortBy : "title";

        String maxAge  = "max-age=" + applicationConfigMessageSource.getMessage(
                "CACHE_MAX_TIME", null, null, null);

        Page<Movie> moviePage = null;

        if (category == null) {

            if (page != null) {
                PageRequest pageRequest = new PageRequest(page, pageSize, Sort.Direction.ASC, sortByProperty);
                moviePage = movieRepository.findAll(pageRequest);
                return ResponseEntity.status(HttpStatus.OK).header("Cache-Control", maxAge).body(moviePage);
            } else {
                List<Movie> movieList = movieRepository.findAll();
                return ResponseEntity.status(HttpStatus.OK).header("Cache-Control", maxAge).body(movieList);
            }
        }
        else {
            MovieType movieType = MovieType.values()[category];

            if (page !=null){

                PageRequest pageRequest = new PageRequest(page, pageSize, Sort.Direction.ASC, sortByProperty);

                if (movieType == MovieType.newest)
                    moviePage = movieRepository.selectAvailableNewest(pageRequest);
                else if (movieType == MovieType.hits)
                    moviePage = movieRepository.selectAvailableHits(pageRequest);
                else if (movieType == MovieType.other)
                    moviePage = movieRepository.selectAvailableOther(pageRequest);

                return ResponseEntity.status(HttpStatus.OK).header("Cache-Control", maxAge).body(moviePage);
            }else {
                List<Movie> movieList = new ArrayList<>();

                movieList = movieRepository.findAll()
                        .stream()
                        .filter(movie -> movie.getType() == movieType)
                        .collect(Collectors.toList());

                return ResponseEntity.status(HttpStatus.OK).header("Cache-Control", maxAge).body(movieList);
            }
        }
    }

    /**
     * Retrives exception message from String's message source
     */
    private String getExceptionMsgSource(String msgCode) {

        return exceptionMessageSource.getMessage(
                msgCode, null, "Something went wrong.", null);
    }
}
