package com.codlex.racememonolith.login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository repository;

    public void addRating(int userId, int rating) {
        User user = this.repository.findById(userId).get();
        user.addRating(rating);
        this.repository.save(user);
        log.debug("{} got {} rating reward.", user, rating);
    }
}
