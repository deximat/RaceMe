package com.codlex.racememonolith.race;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.codlex.racememonolith.login.UserManager;
import com.codlex.racememonolith.race.RaceState.RaceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

public class Race {

	public Runner findRunner(Integer userId) {
		return this.runners.get(userId);
	}

	@AllArgsConstructor
	@Data
	@Slf4j
	public static class Runner {

		private final int id;
		private final String username;
		private volatile double distance;
		private boolean isFinished;

		public synchronized void addDistance(double distance) {
			this.distance += distance;
			double targetDistance = 5;
			this.isFinished = this.distance >= targetDistance;

			if (this.isFinished) {
				log.debug("FINIIIISHED!" + username);
			}
		}

		public Runner(int id) {
			this.id = id;
			this.username = UserManager.get().findById(id).getUsername();
		}
	}

	private static final AtomicInteger ID_GENERATOR = new AtomicInteger();

	@Getter
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



	public RaceState buildState() {
		return new RaceState(this.id, new ArrayList<>(this.runners.values()), RaceStatus.Running);
	}

}
