package com.izbicki.jakub.Entity;


import java.math.BigDecimal;
import java.util.List;

public class RentalDetails {

    private BigDecimal amountToPay;
    private List<Movie> rentedMovies;

    public RentalDetails(BigDecimal amountToPay, List<Movie> rentedMovies) {
        this.amountToPay = amountToPay;
        this.rentedMovies = rentedMovies;
    }

    public BigDecimal getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(BigDecimal amountToPay) {
        this.amountToPay = amountToPay;
    }

    public List<Movie> getRentedMovies() {
        return rentedMovies;
    }

    public void setRentedMovies(List<Movie> rentedMovies) {
        this.rentedMovies = rentedMovies;
    }
}
