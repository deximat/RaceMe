package com.codlex.racememonolith.login;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LoginController {

	@Data
	private static class User {
		private static final AtomicInteger ID_GENERATOR = new AtomicInteger();
		private int id;
		private String username;
		private int rating;

		public User(final String username) {
			this.id = ID_GENERATOR.incrementAndGet();
			this.username = username;
		}
	}


	private final Map<String, User> users = new HashMap<>();

	@RequestMapping(value = "/login/{username}")
	public synchronized User login(@PathVariable("username") String username) {
		log.debug("username {}", username);
		User user = this.users.get(username);

		// create new if one doesn't exist
		if (user == null) {
			user = new User(username);
			this.users.put(username, user);
		}

		return user;
	}


}
