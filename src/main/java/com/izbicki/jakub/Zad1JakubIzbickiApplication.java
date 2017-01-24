package com.izbicki.jakub;

import com.izbicki.jakub.entity.Movie;
import com.izbicki.jakub.entity.User;
import com.izbicki.jakub.repository.ActorRepository;
import com.izbicki.jakub.repository.CastRepository;
import com.izbicki.jakub.repository.MovieRepository;
import com.izbicki.jakub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

import static com.izbicki.jakub.security.WebSecurityConfig.ROLE_ADMIN;
import static com.izbicki.jakub.security.WebSecurityConfig.ROLE_USER;

@SpringBootApplication
//@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class Zad1JakubIzbickiApplication implements CommandLineRunner{

	private static final BigDecimal newestPrice = new BigDecimal(40);
	private static final BigDecimal hitsPrice = new BigDecimal(25);
	private static final BigDecimal otherPrice = new BigDecimal(10);

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

			movieRepository.save(new Movie(
					"n" + i, "desc", MovieType.newest, newestPrice, true));
			movieRepository.save(new Movie(
					"h" + i, "desc", MovieType.hits, hitsPrice, true));
			movieRepository.save(new Movie(
					"o" + i, "desc", MovieType.other, otherPrice, true));

			userRepository.save(new User("user" + i, BigDecimal.ZERO, ROLE_USER));
		}

		userRepository.save(new User("admin", BigDecimal.ZERO, ROLE_ADMIN));
		userRepository.save(new User("user", BigDecimal.ZERO, ROLE_USER));
	}
}
