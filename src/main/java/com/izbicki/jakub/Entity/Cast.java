package com.izbicki.jakub.Entity;

import javax.persistence.*;

/**
 * Table that connects Actors to Movies
 */
@Entity
public class Cast {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name = "movie", referencedColumnName = "id")
    private Movie movie;

    @ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name = "actor", referencedColumnName = "id")
    private Actor actor;

    public Cast() {};

    public Cast(Movie movie, Actor actor) {
        this.movie = movie;
        this.actor = actor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovieId(Movie movie) {
        this.movie = movie;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }
}
