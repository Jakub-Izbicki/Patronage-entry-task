package com.izbicki.jakub;

import com.izbicki.jakub.Entity.Movie;
import com.izbicki.jakub.Entity.User;
import com.izbicki.jakub.Repository.ActorRepository;
import com.izbicki.jakub.Repository.CastRepository;
import com.izbicki.jakub.Repository.MovieRepository;
import com.izbicki.jakub.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Random;

import static com.izbicki.jakub.Security.WebSecurityConfig.ROLE_ADMIN;
import static com.izbicki.jakub.Security.WebSecurityConfig.ROLE_USER;

@SpringBootApplication
//@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class Zad1JakubIzbickiApplication implements CommandLineRunner{

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private ActorRepository actorRepository;

	@Autowired
	private CastRepository castRepository;

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {

		SpringApplication.run(Zad1JakubIzbickiApplication.class, args);
	}


	/**
	 * Populate database with some sample records
	 *
	 * for the sake of simplicity, actors and casts are skipped,
	 * because they are not used in this patronage task(2)
	 */
	@Override
	public void run(String... strings) throws Exception {


		for (int i = 0; i < 10; i++){

			Random random = new Random();

			movieRepository.save(new Movie(
					"n" + i, "desc", MovieType.newest, random.nextInt(50) + 1));
			movieRepository.save(new Movie(
					"h" + i, "desc", MovieType.hits, random.nextInt(50) + 1));
			movieRepository.save(new Movie(
					"o" + i, "desc", MovieType.other, random.nextInt(50) + 1));

			userRepository.save(new User("user" + i, "pass", 0f, ROLE_USER));
		}

		userRepository.save(new User("admin", "pass", 0f, ROLE_ADMIN));
		userRepository.save(new User("user", "pass", 0f, ROLE_USER));
	}
}
