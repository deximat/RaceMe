package com.codlex.raceme.findraceservice;

import com.codlex.raceme.api.RaceService;
import com.codlex.raceme.data.RaceState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@ConfigurationProperties(prefix = "finder")
public class RaceFinder {

	private int numberOfParticipants;

	private RaceService raceService;

	@Autowired
	public RaceFinder(RaceService raceService) {
		this.raceService = raceService;
	}

	private Set<Integer> waitingUsers = new HashSet<>();

	@RequestMapping(value = "/race/cancel/user/{userId}")
	public synchronized boolean cancel(@PathVariable Integer userId) {
		log.debug("#### CANCEL_RACE {}", userId);

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
		log.debug("#### FIND_RACE {}", userId);

		if (this.waitingUsers.contains(userId)) {
			return RaceState.WAITING;
		}

		// check if he is already in race
		RaceState runningRace = this.raceService.findRaceByUserId(userId);
		if (!runningRace.equals(RaceState.NONE)) {
			return runningRace;
		}

		this.waitingUsers.add(userId);

		if (this.waitingUsers.size() >= numberOfParticipants) {
			List<Integer> racingIds = new ArrayList(this.waitingUsers);
			this.waitingUsers.clear();
			return this.raceService.startRace(racingIds);
		} else {
			return RaceState.WAITING;
		}
	}

}
