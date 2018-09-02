package com.codlex.racememonolith.login;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Data;

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

	public synchronized void registerUser(String username) {
		this.users.put(username, new User(username));
	}
}
