package com.codlex.raceme.loginservice.user;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    public User findByUsername(String username);
}
