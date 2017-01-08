package com.izbicki.jakub.Controller;


import com.izbicki.jakub.Entity.Movie;
import com.izbicki.jakub.Entity.User;
import com.izbicki.jakub.Repository.UserRepository;
import com.izbicki.jakub.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@RestController
@RequestMapping(value = "/user", produces = "application/json")
public class UserController {

    @Autowired @Qualifier("UserService")
    private UserService us;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "", method = GET)
    public User selectUser(Principal principal){

        return us.selectUser(principal);
    }

    @RequestMapping(value = "/all")
    public List<User> selectAll(){

        return us.selectAll();
    }

    @RequestMapping(value = "/create", method = POST)
    public User add(@RequestHeader(value = "login") String login,
                    @RequestHeader(value = "password") String password) {

        return us.addUser(login, password, false);
    }

    @RequestMapping(value = "/createAdmin", method = POST)
    public User addAdmin(@RequestHeader(value = "login") String login,
                    @RequestHeader(value = "password") String password) {

        return us.addUser(login, password, true);
    }

    @RequestMapping(value = "/rent", method = POST)
    public List<Movie> rentMovies(@RequestParam(value = "movieId") List<Long> movieIdsList, Principal principal){

        return us.rentMovies(movieIdsList, principal);
    }

    @RequestMapping(value = "/movies", method = GET)
    public List<Movie> selectRentedMovies(Principal principal){

        return us.selectRentedMovies(principal);
    }

    @RequestMapping(value = "/movies/{userId}", method = GET)
    public List<Movie> selectRentedMoviesByUserId(@PathVariable("userId") Long userId){

        return us.selectRentedMoviesByUserId(userId);
    }

    @RequestMapping(value = "/return", method = POST)
    public List<Movie> returnMovies(@RequestParam(value = "movieId") List<Long> movieIdsList, Principal principal){

        return us.returnMovies(movieIdsList, principal);
    }
}
