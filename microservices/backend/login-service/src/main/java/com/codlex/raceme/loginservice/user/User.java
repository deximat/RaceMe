package com.codlex.raceme.loginservice.user;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity(name = "user_r")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private String username;

    private int rating;

    public User(final String username) {
        this.username = username;
    }

    public void addRating(int reward) {
        this.rating = Math.max(0, this.rating + reward);
    }

}


