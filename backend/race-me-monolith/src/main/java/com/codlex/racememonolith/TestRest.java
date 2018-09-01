package com.codlex.racememonolith;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRest {

	private AtomicInteger test = new AtomicInteger();

	@RequestMapping("/hello")
	public String hello() {
		return "Hello!" + test.incrementAndGet();
	}
}
