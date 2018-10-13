package com.codlex.racememonolith.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/login-service/")
public class LoginController {

	@Autowired
	private UserRepository repository;

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

}
