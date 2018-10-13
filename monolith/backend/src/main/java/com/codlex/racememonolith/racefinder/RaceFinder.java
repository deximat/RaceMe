package com.codlex.racememonolith.racefinder;

import com.codlex.racememonolith.race.Race;
import com.codlex.racememonolith.race.RaceManager;
import com.codlex.racememonolith.race.RaceState;
import com.codlex.racememonolith.race.runner.Runner;
import com.codlex.racememonolith.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Slf4j
@RequestMapping("/find-race-service/")
public class RaceFinder {

	private static final int NUMBER_OF_PARTICIPANTS = 1;

	private RaceManager raceManager;

	public RaceFinder(RaceManager raceManager) {
		this.raceManager = raceManager;
	}

	private Set<Integer> waitingUsers = new HashSet<>();

	@RequestMapping(value = "/race/cancel/user/{userId}")
	public synchronized boolean cancel(@PathVariable Integer userId) {
		boolean result = this.waitingUsers.remove(userId);
		if (result) {
			log.debug("{} successfully canceled finding race.", userId);
		} else {
			log.error("{} tried to cancel race but wasn't in queue.", userId);
		}
		return result;
	}

	@RequestMapping(value = "/race/find/user/{userId}")
	public synchronized RaceState findRace(@PathVariable("userId") int userId) {

		// check if he is already in race
		RaceState runningRace = this.raceManager.findRaceByUserId(userId);
		if (runningRace != null) {
			return runningRace;
		}

		this.waitingUsers.add(userId);

		if (this.waitingUsers.size() >= NUMBER_OF_PARTICIPANTS) {
			List<Integer> racingIds = new ArrayList(this.waitingUsers);
			this.waitingUsers.clear();
			return this.raceManager.startRace(racingIds);
		} else {
			return RaceState.WAITING;
		}
	}

}
