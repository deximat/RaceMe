package com.codlex.raceme.findraceservice;

import com.codlex.raceme.api.RaceService;
import com.codlex.raceme.data.RaceState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/find-race-service/")
public class RaceFinder {

	private static final int NUMBER_OF_PARTICIPANTS = 1;

	private RaceService raceService;

	@Autowired
	public RaceFinder(RaceService raceService) {
		this.raceService = raceService;
	}

	private Set<Integer> waitingUsers = new HashSet<>();

	@RequestMapping(value = "/race/cancel/user/{userId}")
	public synchronized boolean cancel(@PathVariable Integer userId) {
		boolean result = this.waitingUsers.remove(userId);
		if (result) {
//			log.debug("{} successfully canceled finding race.", userId);
		} else {
//			log.error("{} tried to cancel race but wasn't in queue.", userId);
		}
		return result;
	}

	@RequestMapping(value = "/race/find/user/{userId}")
	public synchronized RaceState findRace(@PathVariable("userId") int userId) {

		// check if he is already in race
		RaceState runningRace = this.raceService.findRaceByUserId(userId);
		if (runningRace != null) {
			return runningRace;
		}

		this.waitingUsers.add(userId);

		if (this.waitingUsers.size() >= NUMBER_OF_PARTICIPANTS) {
			List<Integer> racingIds = new ArrayList(this.waitingUsers);
			this.waitingUsers.clear();
			return this.raceService.startRace(racingIds);
		} else {
			return RaceState.WAITING;
		}
	}

}
