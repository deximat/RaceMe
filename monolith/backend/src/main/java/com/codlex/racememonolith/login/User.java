package com.codlex.racememonolith.login;

import lombok.Data;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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


