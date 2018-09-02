package com.codlex.racememonolith.race;

import java.util.*;

import com.codlex.racememonolith.race.Race.Runner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class RaceManager {
	private static final int NUMBER_OF_PARTICIPANTS = 2;

	private Map<Integer, Race> races = new HashMap<>();

	public RaceManager() {
	}

	@RequestMapping(value = "/race/{raceId}/user/{userId}/distance/{distance}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public RaceState updateDistance(@PathVariable("raceId") Integer raceId, @PathVariable("userId") Integer userId,
			@PathVariable(value = "distance", required = false) Double distance) {
		log.debug("raceId: {}, userId: {}, distance: {}", raceId, userId, distance);
		final Race race = findRaceById(raceId);
		final Runner runner = race.findRunner(userId);
		runner.addDistance(distance);
		return race.buildState();
	}

	private synchronized Race findRaceById(final int raceId) {
		return this.races.get(raceId);
	}

	private Set<Integer> waitingUsers = new HashSet<>();

	@RequestMapping(value = "/race/find/user/{userId}")
	public synchronized RaceState findRace(@PathVariable("userId") int userId) {

		// check if he is already in race
		Race runningRace = findRaceByUserId(userId);
		if (runningRace != null) {
			return runningRace.buildState();
		}

		this.waitingUsers.add(userId);

		if (this.waitingUsers.size() >= NUMBER_OF_PARTICIPANTS) {
			List<Integer> racingIds = new ArrayList(this.waitingUsers);
			this.waitingUsers.clear();
			return startRace(racingIds).buildState();
		} else {
			return RaceState.WAITING;
		}

	}

	private Race findRaceByUserId(int userId) {
		for (Race race : this.races.values()) {
			if (race.findRunner(userId) != null) {
				return race;
			}
		}
		return null;
	}

	private Race startRace(List<Integer> racingIds) {
		Race race = new Race(racingIds);
		this.races.put(race.getId(), race);
		return race;
	}

}
