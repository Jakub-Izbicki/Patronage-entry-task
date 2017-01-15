package com.izbicki.jakub.Entity;


import com.izbicki.jakub.MovieType;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.ORDINAL)
    private MovieType type;

    private BigDecimal price;

    private Boolean isAvailable;

    @ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name = "user", referencedColumnName = "id")
    private User rentedBy;


    protected Movie() {}

    public Movie(MovieType type, BigDecimal price, Boolean isAvailable) {
        this.title = null;
        this.description = null;
        this.type = type;
        this.price = price;
        this.isAvailable = isAvailable;
        this.rentedBy = null;
    }

    public Movie(String title, String description, MovieType type, BigDecimal price, Boolean isAvailable) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.price = price;
        this.isAvailable = isAvailable;
        this.rentedBy = null;
    }

    public User getRentedBy() {
        return rentedBy;
    }

    public void setRentedBy(User rentedBy) {
        this.rentedBy = rentedBy;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean avaliable) {
        isAvailable = avaliable;
    }

    public MovieType getType() {
        return type;
    }

    public void setType(MovieType type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
