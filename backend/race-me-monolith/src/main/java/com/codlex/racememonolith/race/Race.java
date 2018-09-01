package com.codlex.racememonolith.race;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

public class Race {

	public Runner findRunner(Integer userId) {
		return this.runners.get(userId);
	}

	@AllArgsConstructor
	@Data
	public static class Runner {
		@Getter
		private final int id;

		@Getter
		private volatile double distance = 3;

		public synchronized void addDistance(double distance) {
			this.distance += distance;
		}

		public Runner(int id) {
			this.id = id;
		}
	}

	private static final AtomicInteger ID_GENERATOR = new AtomicInteger();

	final int id;
	final Map<Integer, Runner> runners;

	public Race(final List<Integer> userIds) {
		this.id = ID_GENERATOR.incrementAndGet();
		this.runners = buildRunners(userIds);
	}

	private Map<Integer, Runner> buildRunners(List<Integer> userIds) {
		Map<Integer, Runner> runners = new HashMap<>();
		for (Integer userId : userIds) {
			runners.put(userId, new Runner(userId));
		}
		return runners;
	}

	@AllArgsConstructor
	@Data
	public class RaceState {
		final int id;
		final List<Runner> runners;
	}

	public RaceState buildState() {
		return new RaceState(this.id, new ArrayList<>(this.runners.values()));
	}

}
