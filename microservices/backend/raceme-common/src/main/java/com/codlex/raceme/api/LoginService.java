package com.codlex.raceme.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("login-service")
public interface LoginService {
    @RequestMapping("/add-rating/user/{userId}/{amount}")
    void addRating(@PathVariable("userId") int userId, @PathVariable("amount") int amount);
}
