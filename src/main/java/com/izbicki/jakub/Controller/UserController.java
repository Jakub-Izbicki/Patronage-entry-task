package com.izbicki.jakub.Controller;


import com.izbicki.jakub.Entity.User;
import com.izbicki.jakub.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping(value = "/user", produces = "application/json")
public class UserController {

    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @Autowired
    public UserController(InMemoryUserDetailsManager inMemoryUserDetailsManager) {
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
    }

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/all")
    public List<User> selectAll(){

        List<User> userList = new ArrayList<>();

        for(User user : userRepository.findAll()){

            userList.add(user);
        }

        return userList;
    }

//    @RequestMapping("exists/{username}")
//    public boolean userExists(@PathVariable("username") String username ) {
//        return inMemoryUserDetailsManager.userExists(username);
//    }

    @RequestMapping("add/{login}/{password}")
    public User add(@PathVariable("login") String login,
                    @PathVariable("password") String password) {

        GrantedAuthority sga = new SimpleGrantedAuthority("ROLE_USER");

        inMemoryUserDetailsManager.createUser(
                new org.springframework.security.core.userdetails.User(login, password,
                    new ArrayList<GrantedAuthority>(Arrays.asList(sga))));

        return new User(login, password, 0f, "ROLE_USER");
    }
}
