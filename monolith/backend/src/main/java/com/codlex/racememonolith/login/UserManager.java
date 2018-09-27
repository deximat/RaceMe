package com.codlex.racememonolith.login;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
public class UserManager {

	private static final UserManager INSTANCE = new UserManager();

	public static UserManager get() {
		return INSTANCE;
	}

	@Data
	public static class User {
		private static final AtomicInteger ID_GENERATOR = new AtomicInteger();
		private int id;
		private String username;
		private int rating;

		public User(final String username) {
			this.id = ID_GENERATOR.incrementAndGet();
			this.username = username;
		}

        public void addRating(int reward) {
			this.rating = Math.max(0, this.rating + reward);
        }
    }

	private final Map<String, User> users = new HashMap<>();

	public synchronized User findById(int id) {
		for (User user : users.values()) {
			if (id == user.getId()) {
				return user;
			}
		}

		return null;
	}

	public synchronized User findByUsername(String username) {
		return this.users.get(username);
	}

	public synchronized User registerUser(String username) {
		final User user = new User(username);
		this.users.put(username, user);
		log.debug("Registered {}.", user);
		return user;
	}

}
