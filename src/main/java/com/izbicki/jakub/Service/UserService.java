package com.izbicki.jakub.Service;

import com.izbicki.jakub.Entity.Movie;
import com.izbicki.jakub.Entity.RentalDetails;
import com.izbicki.jakub.Entity.User;
import com.izbicki.jakub.Error.ApiCustomException;
import com.izbicki.jakub.Error.ErrorCodes;
import com.izbicki.jakub.MovieType;
import com.izbicki.jakub.Repository.MovieRepository;
import com.izbicki.jakub.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.izbicki.jakub.Security.WebSecurityConfig.ROLE_ADMIN;
import static com.izbicki.jakub.Security.WebSecurityConfig.ROLE_USER;

@Component("UserService")
public class UserService {

    @Autowired
    private ResourceBundleMessageSource exceptionMessageSource;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @Autowired
    public UserService(InMemoryUserDetailsManager inMemoryUserDetailsManager) {
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
    }

    private User selectPrincipal(Principal principal){

        String login = principal.getName();

        List<User> userList = userRepository.selectUserByLogin(login);

        if (userList.size() == 0)
            throw new ApiCustomException(HttpStatus.BAD_REQUEST,
                    getExceptionMsgSource(ErrorCodes.USER_NOT_FOUND),
                    getExceptionMsgSource(ErrorCodes.USER_NOT_FOUND_USER));

        return userList.get(0);
    }

    public ResponseEntity selectUser(Principal principal){

        User user = selectPrincipal(principal);

        return ResponseEntity.ok(user);
    }

    public ResponseEntity selectAll(){

        List<User> userList = new ArrayList<>();

        for(User user : userRepository.findAll())
            userList.add(user);

        return ResponseEntity.ok(userList);
    }

    private Boolean isUserExists(String login){

        return inMemoryUserDetailsManager.userExists(login);
    }

    public ResponseEntity addUser(String login, String password, Boolean isAdmin){

        if (isUserExists(login))
            throw new ApiCustomException(HttpStatus.BAD_REQUEST,
                    getExceptionMsgSource(ErrorCodes.UNAVAILABLE_LOGIN),
                    getExceptionMsgSource(ErrorCodes.UNAVAILABLE_LOGIN_USER));

        String role;

        if (isAdmin)
          role = ROLE_ADMIN;
        else
            role = ROLE_USER;

        GrantedAuthority sga = new SimpleGrantedAuthority(role);

        inMemoryUserDetailsManager.createUser(
                new org.springframework.security.core.userdetails.User(
                        login, password, new ArrayList<GrantedAuthority>(Arrays.asList(sga))));

        User user = new User(login, new BigDecimal(0), role);

        userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    public ResponseEntity selectRentedMovies(Principal principal){

        User user = selectPrincipal(principal);

        return ResponseEntity.ok(movieRepository.selectRentedMovies(user));
    }

    public ResponseEntity rentMovies(List<Long> moviesIds, Principal principal){

        User user = selectPrincipal(principal);

        if (!isAllMoviesExist(moviesIds))
            throw new ApiCustomException(HttpStatus.BAD_REQUEST,
                    getExceptionMsgSource(ErrorCodes.MOVIES_NOT_FOUND),
                    getExceptionMsgSource(ErrorCodes.MOVIES_NOT_FOUND_USER));

        List<Movie> moviesToRentJpa = movieRepository.getMoviesByIds(moviesIds);

        if (!isAllMoviesAvaliable(moviesToRentJpa))
            throw new ApiCustomException(HttpStatus.BAD_REQUEST,
                    getExceptionMsgSource(ErrorCodes.MOVIES_NOT_AVAILABLE),
                    getExceptionMsgSource(ErrorCodes.MOVIES_NOT_AVAILABLE_USER));

        if (!canRent(user, moviesIds.size()))
            throw new ApiCustomException(HttpStatus.BAD_REQUEST,
                    getExceptionMsgSource(ErrorCodes.MAX_RENT_EXC),
                    getExceptionMsgSource(ErrorCodes.MAX_RENT_EXC_USER));


        //copy movies into new list so jpa won't persist changed prices of movies
        List<Movie> moviesToRent = createMovieListFromAnother(moviesToRentJpa);

        BigDecimal amountToPay = new BigDecimal(0);

        //every 3 films user gets one "other" for free
        moviesToRent = calculateFreeMovies(moviesToRent);

        for (Movie movie : moviesToRent)
            amountToPay = amountToPay.add(movie.getPrice());

        //if has at least 2 "newest" - then has 25% off
        amountToPay = calculateDiscount(moviesToRent, amountToPay);

        updateWallet(user.getLogin(), user.getRentalWallet().add(amountToPay));

        movieRepository.rentMovies(user, moviesIds);

        RentalDetails rentalDetails = new RentalDetails(amountToPay, movieRepository.selectMoviesWhereId(moviesIds));

        return ResponseEntity.ok(rentalDetails);
    }

    public ResponseEntity selectRentedMoviesByUserId(Long userId){

        User user = userRepository.findOne(userId);

        return ResponseEntity.ok(movieRepository.selectRentedMovies(user));
    }

    public ResponseEntity returnMovies(List<Long> moviesIds, Principal principal){

        User user = selectPrincipal(principal);

        if (!isAllReturnedMoviesAreRented(moviesIds, user))
            throw new ApiCustomException(HttpStatus.BAD_REQUEST,
                    getExceptionMsgSource(ErrorCodes.MOVIES_NOT_RENTED),
                    getExceptionMsgSource(ErrorCodes.MOVIES_NOT_RENTED_USER));

        movieRepository.returnMovies(moviesIds);

        return ResponseEntity.ok(movieRepository.selectRentedMovies(user));
    }

    private void updateWallet(String login, BigDecimal walletValue){

        userRepository.updateWallet(login, walletValue);
    }

    /**
     * Checks whether the user can rent the specified amount of movies,
     * Any user can have the maximum of 10 rented movies at the same time.
     */
    private boolean canRent(User user, int numberToRent){

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

            if (movie.getType() == MovieType.other &&
                    (movie.getPrice().signum() != 0)){

                movie.setPrice(BigDecimal.ZERO);
                counter++;
            }

            if (counter >= numberOfFreeMovies)
                return movieList;
        }

        return movieList;
    }

    private BigDecimal calculateDiscount(List<Movie> movieList, BigDecimal currentWalletValue){

        int counter = 0;

        for (Movie movie : movieList){

            if (movie.getType() == MovieType.newest)
                counter++;

            if (counter >= 2){
                BigDecimal discount = currentWalletValue.multiply(new BigDecimal("0.25"));
                return currentWalletValue.subtract(discount);
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
            newMovieList.add(new Movie(movie.getType(), movie.getPrice(), movie.getIsAvailable()));
        }

        return newMovieList;
    }

    /**
     * Checks whether all selected movies are avaliable for rental
     */
    private boolean isAllMoviesAvaliable(List<Movie> movieList){

        for (Movie movie : movieList){

            if (Boolean.FALSE.compareTo(movie.getIsAvailable()) == 0)
                return false;
        }

        return true;
    }

    /**
     * Checks whether all given movies exist in database
     */
    private boolean isAllMoviesExist(List<Long> movieIdList){

        for (Long movieId : movieIdList){
            if (movieRepository.findOne(movieId) == null)
                return false;
        }

        return true;
    }

    /**
     * Checks whether all movies that are to be returned are rented by the user
     */
    private boolean isAllReturnedMoviesAreRented(List<Long> movieIdList, User user){

        List<Movie> moviesFromDb = movieRepository.selectRentedMovies(user);

        List<Long> idsFromDb = new ArrayList<>();

        for (Movie movie : moviesFromDb)
            idsFromDb.add(movie.getId());

        for (Long id : movieIdList){

            if (!idsFromDb.contains(id))
                return false;
        }

        return true;
    }

    /**
     * Retrives exception message from String's message source
     */
    private String getExceptionMsgSource(String msgCode){

        return exceptionMessageSource.getMessage(
                msgCode, null, "Something went wrong.", null);
    }
}
