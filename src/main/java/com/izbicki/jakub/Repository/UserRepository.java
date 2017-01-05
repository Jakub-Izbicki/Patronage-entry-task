package com.izbicki.jakub.Repository;


import com.izbicki.jakub.Entity.User;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, Long> {

}
