package com.izbicki.jakub.Service;

import com.izbicki.jakub.Entity.Movie;
import com.izbicki.jakub.Entity.User;
import com.izbicki.jakub.MovieType;
import com.izbicki.jakub.Repository.MovieRepository;
import com.izbicki.jakub.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.izbicki.jakub.Security.WebSecurityConfig.ROLE_ADMIN;
import static com.izbicki.jakub.Security.WebSecurityConfig.ROLE_USER;

@Component("UserService")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @Autowired
    public UserService(InMemoryUserDetailsManager inMemoryUserDetailsManager) {
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
    }

    public User selectUser(Principal principal){

        String login = principal.getName();

        List<User> userList = userRepository.selectUserByLogin(login);

        if (userList.size() == 0)
            return null;

        return userList.get(0);
    }

    public List<User> selectAll(){

        List<User> userList = new ArrayList<>();

        for(User user : userRepository.findAll())
            userList.add(user);

        return userList;
    }

    private Boolean isUserExists(String login){

        return inMemoryUserDetailsManager.userExists(login);
    }

    public User addUser(String login, String password, Boolean isAdmin){

        if (isUserExists(login))
            return null; //login is unavailable

        String role;

        if (isAdmin)
          role = ROLE_ADMIN;
        else
            role = ROLE_USER;

        GrantedAuthority sga = new SimpleGrantedAuthority(role);

        inMemoryUserDetailsManager.createUser(
                new org.springframework.security.core.userdetails.User(
                        login, password, new ArrayList<GrantedAuthority>(Arrays.asList(sga))));

        User user = new User(login, 0f, role);

        userRepository.save(user);

        return user;
    }

    public List<Movie> selectRentedMovies(Principal principal){

        User user = selectUser(principal);

        return movieRepository.selectRentedMovies(user);
    }

    public List<Movie> rentMovies(List<Long> moviesIds, Principal principal){

        User user = selectUser(principal);

        if (!canRent(user, moviesIds.size()))
            return null; //can only rent max of ten movies

        List<Movie> moviesToRentJpa = movieRepository.getMoviesByIds(moviesIds);

        //copy movies into new list so jpa won't persist changed prices of movies
        List<Movie> moviesToRent = createMovieListFromAnother(moviesToRentJpa);

        float newWalletValue = 0;

        //every 3 films user gets one "other" for free
        moviesToRent = calculateFreeMovies(moviesToRent);

        for (Movie movie : moviesToRent)
            newWalletValue += movie.getPrice();

        //if has at least 2 "newest" - then has 25% off
        newWalletValue = calculateDiscount(moviesToRent, newWalletValue);

        updateWallet(user.getLogin(), user.getRentalWallet() + newWalletValue);

        movieRepository.rentMovies(user, moviesIds);

        return selectRentedMovies(principal);
    }

    public List<Movie> selectRentedMoviesByUserId(Long userId){

        User user = userRepository.findOne(userId);

        return movieRepository.selectRentedMovies(user);
    }

    public List<Movie> returnMovies(List<Long> moviesIds, Principal principal){

        User user = selectUser(principal);

        movieRepository.returnMovies(moviesIds);

        return movieRepository.selectRentedMovies(user);
    }

    private void updateWallet(String login, float walletValue){

        userRepository.updateWallet(login, walletValue);
    }

    /**
     * Checks whether the user can rent the specified amount of movies,
     * Any user can have the maximum of 10 rented movies at the same time.
     */
    private Boolean canRent(User user, int numberToRent){

        List<Movie> movies = movieRepository.selectRentedMovies(user);

        int numberRented = movies.size();

        return (numberRented + numberToRent) <= 10;
    }

    /**
     * Checks how many movies the user can rent for free (every three movies, the user can rent one from "other" for free)
     * and sets their price to 0.
     */
    private List<Movie> calculateFreeMovies(List<Movie> movieList){

        List<Movie> moviesNewestHits = new ArrayList<Movie>();
        List<Movie> moviesOther = new ArrayList<Movie>();

        for (Movie movie : movieList){

            if (movie.getType() != MovieType.other)
                moviesNewestHits.add(movie);
            else
                moviesOther.add(movie);
        }

        //no movies from "other" category
        if (moviesOther.size() == 0)
            return movieList;

        //get the available number of movies from "other" category to rent for free
        //every three movies, one from "other" can be rented for free
        int numberOfFreeMovies = moviesNewestHits.size()/3;

        movieList = setOtherMoviesFree(movieList, numberOfFreeMovies);

        if (numberOfFreeMovies == moviesOther.size())
            return movieList;

        //take under consideration all remaining "hits" and "newest" movies (if any)
        int remainingNewestHits =  moviesNewestHits.size() - (numberOfFreeMovies * 3);
        //take under consideration all remaining "other" movies that werent marked as free
        int remainingOther = moviesOther.size() - numberOfFreeMovies;

        int fullRemaining = remainingNewestHits + remainingOther;

        if (fullRemaining % 3 == 0)
            fullRemaining--;

        int numberOfRemainingFreeMovies = fullRemaining/3;

        return setOtherMoviesFree(movieList, numberOfRemainingFreeMovies);
    }

    /**
     * Sets the price to 0 in the given number of "other" films in the list.
     */
    private List<Movie> setOtherMoviesFree(List<Movie> movieList, int numberOfFreeMovies){

        int counter = 0;

        for (Movie movie : movieList){

            if (movie.getType() == MovieType.other && movie.getPrice() != 0f){

                movie.setPrice(0f);
                counter++;
            }

            if (counter >= numberOfFreeMovies)
                return movieList;
        }

        return movieList;
    }

    private float calculateDiscount(List<Movie> movieList, float currentWalletValue){

        int counter = 0;

        for (Movie movie : movieList){

            if (movie.getType() == MovieType.newest)
                counter++;

            if (counter >= 2){
                float discount = 0.25f * currentWalletValue;
                return currentWalletValue - discount;
            }
        }

        return currentWalletValue;
    }

    /**
     * Copies a list of movie objects
     */
    private List<Movie> createMovieListFromAnother(List<Movie> sourceMovieList){

        List<Movie> newMovieList = new ArrayList<>();

        for (Movie movie : sourceMovieList){
            newMovieList.add(new Movie(movie.getType(), movie.getPrice(), movie.getAvailable()));
        }

        return newMovieList;
    }
}
