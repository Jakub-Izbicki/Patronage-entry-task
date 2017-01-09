package com.izbicki.jakub.Service;


import com.izbicki.jakub.Entity.Actor;
import com.izbicki.jakub.Entity.Cast;
import com.izbicki.jakub.Entity.Movie;
import com.izbicki.jakub.MovieType;
import com.izbicki.jakub.Repository.ActorRepository;
import com.izbicki.jakub.Repository.CastRepository;
import com.izbicki.jakub.Repository.MovieRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MovieServiceTest {



    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ActorRepository actorRepository;

    @Mock
    private CastRepository castRepository;

    @Autowired
    private MovieService movieService;

    @Before
    public void setUp(){

        MockitoAnnotations.initMocks(this);

        movieRepository.save(new Movie("Atak pająków", "Oskar za fabułę", MovieType.newest, 10f, true));
        movieRepository.save(new Movie("Atak pająków 2", "Zmutowane pająki", MovieType.newest, 10f, true));
        movieRepository.save(new Movie("Atak pająków 3", "Zakończenie epickiej trylogii.", MovieType.newest, 10f, true));

        actorRepository.save(new Actor("Bruce Willis"));
        actorRepository.save(new Actor("Jackie Chan"));

        castRepository.save(new Cast(movieRepository.findOne(1L), actorRepository.findOne(1L)));
        castRepository.save(new Cast(movieRepository.findOne(1L), actorRepository.findOne(2L)));
    }

    @Test
    public void testFindAll(){

        List<Movie> movieList = movieService.selectAll();

        Assert.assertNotNull("Failure - expected not null", movieList);
        Assert.assertEquals("Failure - expected list size", 3,  movieList.size());
    }

    @Test
    public void testFindOne(){

        Long id = 1L;

        Movie movie = movieService.selectMovie(id);

        Assert.assertNotNull("failure - expected not null", movie);
        Assert.assertEquals("failure - expected id attribute match", id,
                movie.getId());
    }

    @Test
    public void testFindOneNotFound() {

        Long id = Long.MAX_VALUE;

        Movie movie = movieService.selectMovie(id);

        Assert.assertNull("failure - expected null", movie);
    }

    @Test
    public void testInsert() {

        Movie createdMovie = movieService.insert("title1", "desc1", MovieType.newest, 10f);

        Assert.assertNotNull("failure - expected not null", createdMovie);
        Assert.assertNotNull("failure - expected id attribute not null", createdMovie.getId());
        Assert.assertEquals("failure - expected text attribute match", "title1", createdMovie.getTitle());

        List<Movie> movieList = movieService.selectAll();

        Assert.assertEquals("failure - expected size", 4, movieList.size());
    }

    @Test
    public void testUpdate() {

        Long id = 1L;

        Movie movie = movieService.selectMovie(id);

        Assert.assertNotNull("failure - expected not null", movie);

        Movie updatedMovie = movieService.update(id, "title2", null);

        Assert.assertNotNull("failure - expected not null", updatedMovie);
        Assert.assertEquals("failure - expected id attribute match", id,
                updatedMovie.getId());
    }

    @Test
    public void testDelete() {

        Long id = 1L;

        Movie movie = movieService.selectMovie(id);

        Assert.assertNotNull("failure - expected not null", movie);

        movieService.remove(id);

        List<Movie> movieList = movieService.selectAll();

        Assert.assertEquals("failure - expected size", 2, movieList.size());

        Movie deletedMovie = movieService.selectMovie(id);

        Assert.assertNull("failure - expected null", deletedMovie);

    }
}