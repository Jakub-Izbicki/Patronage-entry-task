package com.izbicki.jakub.Controller;


import com.izbicki.jakub.Repository.UserRepository;
import com.izbicki.jakub.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;


@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XHTML_XML_VALUE})
public class UserController {

    @Autowired @Qualifier("UserService")
    private UserService us;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/users/current", method = GET)
    public ResponseEntity selectUser(Principal principal){

        return us.selectUser(principal);
    }

    @RequestMapping(value = "/admin/users", method = GET)
    public ResponseEntity selectAll(){

        return us.selectAll();
    }

    @RequestMapping(value = "/users", method = POST)
    public ResponseEntity add(@RequestHeader(value = "login") String login,
                              @RequestHeader(value = "password") String password) {

        return us.addUser(login, password, false);
    }

    @RequestMapping(value = "/admin/users", method = POST)
    public ResponseEntity addAdmin(@RequestHeader(value = "login") String login,
                                   @RequestHeader(value = "password") String password) {

        return us.addUser(login, password, true);
    }

    @RequestMapping(value = "/users/movies", method = POST)
    public ResponseEntity rentMovies(@RequestParam(value = "movieId") List<Long> movieIdsList, Principal principal){

        return us.rentMovies(movieIdsList, principal);
    }

    @RequestMapping(value = "/users/movies", method = GET)
    public ResponseEntity selectRentedMovies(Principal principal){

        return us.selectRentedMovies(principal);
    }

    @RequestMapping(value = "/users/movies/{userId}", method = GET)
    public ResponseEntity selectRentedMoviesByUserId(@PathVariable("userId") Long userId){

        return us.selectRentedMoviesByUserId(userId);
    }

    @RequestMapping(value = "/users/movies", method = DELETE)
    public ResponseEntity returnMovies(@RequestParam(value = "movieId") List<Long> movieIdsList, Principal principal){

        return us.returnMovies(movieIdsList, principal);
    }
}
