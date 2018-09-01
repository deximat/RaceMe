package com.codlex.racememonolith.race;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.websocket.server.PathParam;

import com.codlex.racememonolith.race.Race.RaceState;
import com.codlex.racememonolith.race.Race.Runner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class RaceManager {

	private Map<Integer, Race> races = new HashMap<>();

	public RaceManager() {
		List<Integer> userIds = new ArrayList<>();
		userIds.add(0);
		userIds.add(1);
		this.races.put(1, new Race(userIds));
	}

	@RequestMapping(value = "/race/{raceId}/user/{userId}/distance/{distance}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public RaceState updateDistance(@PathVariable("raceId") Integer raceId, @PathVariable("userId") Integer userId, @PathVariable(value = "distance", required = false) Double distance) {
		log.debug("raceId: {}, userId: {}, distance: {}", raceId, userId, distance);
		final Race race = findRace(raceId);
		final Runner runner = race.findRunner(userId);
		runner.addDistance(distance);
		return race.buildState();
	}

	private synchronized Race findRace(final int raceId) {
		return this.races.get(raceId);
	}
}
