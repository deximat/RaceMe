package com.codlex.racememonolith.login;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    public User findByUsername(String username);
}
