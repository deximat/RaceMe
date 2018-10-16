package com.codlex.raceme.loginservice.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LoginController implements com.codlex.raceme.api.LoginService {


    @Autowired
    private UserRepository repository;

    @RequestMapping(value = "/test")
    public String test() {
        return "TEST IS DONE.";
    }

    @RequestMapping(value = "/login/{username}")
    public User login(@PathVariable("username") String username) {
        log.debug("Login or registration with: {}", username);

        User user = this.repository.findByUsername(username);

        // create new if one doesn't exist
        if (user == null) {
            user = new User(username);
            repository.save(user);
        }
        return user;
    }

    @Override
    public void addRating(@PathVariable("userId") int userId, @PathVariable("amount") int amount) {
        User user = this.repository.findById(userId).get();
        user.addRating(amount);
        this.repository.save(user);
        log.debug("{} got {} rating reward.", user, amount);


    }
}
