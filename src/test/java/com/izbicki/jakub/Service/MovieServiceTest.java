package com.izbicki.jakub.Service;


import com.izbicki.jakub.Entity.Actor;
import com.izbicki.jakub.Entity.Cast;
import com.izbicki.jakub.Entity.Movie;
import com.izbicki.jakub.Repository.ActorRepository;
import com.izbicki.jakub.Repository.CastRepository;
import com.izbicki.jakub.Repository.MovieRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MovieServiceTest {

    @InjectMocks
    MovieRepository movieRepository;

    @InjectMocks
    ActorRepository actorRepository;

    @InjectMocks
    CastRepository castRepository;

    @InjectMocks
    MovieService movieService;

    @Before
    public void populateDb(){

        movieRepository.save(new Movie("Atak pająków", "Oskar za fabułę"));
        movieRepository.save(new Movie("Atak pająków 2", "Zmutowane pająki"));
        movieRepository.save(new Movie("Atak pająków 3", "Zakończenie epickiej trylogii."));

        actorRepository.save(new Actor("Bruce Willis"));
        actorRepository.save(new Actor("Jackie Chan"));
        actorRepository.save(new Actor("Brad Pitt"));
        actorRepository.save(new Actor("SpiderMan"));
        actorRepository.save(new Actor("John Lennon"));

        castRepository.save(new Cast(movieRepository.findOne(1L), actorRepository.findOne(1L)));
        castRepository.save(new Cast(movieRepository.findOne(1L), actorRepository.findOne(2L)));
        castRepository.save(new Cast(movieRepository.findOne(1L), actorRepository.findOne(3L)));
        castRepository.save(new Cast(movieRepository.findOne(2L), actorRepository.findOne(4L)));
        castRepository.save(new Cast(movieRepository.findOne(2L), actorRepository.findOne(5L)));
        castRepository.save(new Cast(movieRepository.findOne(3L), actorRepository.findOne(2L)));
    }

}
