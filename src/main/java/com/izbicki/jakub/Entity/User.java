package com.izbicki.jakub.Entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String login;
    private BigDecimal rentalWallet;
    private String role;

    public User(){};

    public User(String login, BigDecimal rentalWallet, String role) {
        this.login = login;
        this.rentalWallet = rentalWallet;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public BigDecimal getRentalWallet() {
        return rentalWallet;
    }

    public void setRentalWallet(BigDecimal rentalWallet) {
        this.rentalWallet = rentalWallet;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
