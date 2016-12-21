package com.izbicki.jakub;

import com.izbicki.jakub.Entity.Actor;
import com.izbicki.jakub.Entity.Cast;
import com.izbicki.jakub.Entity.Movie;
import com.izbicki.jakub.Repository.ActorRepository;
import com.izbicki.jakub.Repository.CastRepository;
import com.izbicki.jakub.Repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Zad1JakubIzbickiApplication implements CommandLineRunner{

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private ActorRepository actorRepository;

	@Autowired
	private CastRepository castRepository;

	public static void main(String[] args) {

		SpringApplication.run(Zad1JakubIzbickiApplication.class, args);
	}


	/**
	 * Populate database with some sample records
	 */
	@Override
	public void run(String... strings) throws Exception {

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
