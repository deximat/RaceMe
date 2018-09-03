package com.codlex.racememonolith.login;

import com.codlex.racememonolith.login.UserManager.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LoginController {

	@RequestMapping(value = "/login/{username}")
	public User login(@PathVariable("username") String username) {
		log.debug("username {}", username);
		User user = UserManager.get().findByUsername(username);

		// create new if one doesn't exist
		if (user == null) {
			user = UserManager.get().registerUser(username);
		}

		return user;
	}

}
