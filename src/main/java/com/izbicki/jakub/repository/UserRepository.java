package com.izbicki.jakub.repository;


import com.izbicki.jakub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Transactional
    @Query("SELECT u FROM User u WHERE u.login = :login")
    List<User> selectUserByLogin(@Param("login") String login);

    @Modifying
    @Transactional
    @Query("UPDATE User SET rentalWallet = :walletValue WHERE login = :login")
    void updateWallet(@Param("login") String login, @Param("walletValue") BigDecimal walletValue);
}
