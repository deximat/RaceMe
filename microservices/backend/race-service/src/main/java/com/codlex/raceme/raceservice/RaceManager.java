package com.codlex.raceme.raceservice;

import com.codlex.raceme.api.LoginService;
import com.codlex.raceme.data.RaceState;
import com.codlex.raceme.data.Runner;
import com.codlex.raceme.data.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Slf4j
public class RaceManager {

    private final Map<Integer, Race> races = new HashMap<>();

    private Server serverInfo;

    private LoginService loginService;

    @Autowired
    public RaceManager(final Environment environment, final LoginService loginService) {
//        try {
            this.serverInfo = new Server("localhost", Integer.parseInt(environment.getProperty("server.port")));
            log.debug("Server info: " + this.serverInfo);
            this.loginService = loginService;
            log.debug("Login service: " + loginService);
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
    }

    @RequestMapping(value = "/race/{raceId}/user/{userId}/quit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public RaceState updateDistance(@PathVariable("raceId") Integer raceId, @PathVariable("userId") Integer userId) {
        log.debug("raceId: {}, userId: {}, wants to quit: {}", raceId, userId);

        final Race race = findRaceById(raceId);
        final Runner runner = race.findRunner(userId);
        runner.quit();
        return race.buildState(this.serverInfo);
    }

    @RequestMapping(value = "/race/{raceId}/user/{userId}/distance/{distance}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public RaceState updateDistance(@PathVariable("raceId") Integer raceId, @PathVariable("userId") Integer userId,
                                    @PathVariable(value = "distance", required = false) Double distance) {
        log.debug("raceId: {}, userId: {}, distance: {}", raceId, userId, distance);
        final Race race = findRaceById(raceId);
        final Runner runner = race.findRunner(userId);
        runner.addDistance(distance);
        return race.buildState(this.serverInfo);
    }

    private synchronized Race findRaceById(final int raceId) {
        return this.races.get(raceId);
    }

    private Set<Integer> waitingUsers = new HashSet<>();

    public RaceState findRaceByUserId(int userId) {

        for (Race race : this.races.values()) {
            Runner runner = race.findRunner(userId);
            if (runner != null && !runner.isFinished()) {
                return race.buildState(this.serverInfo);
            }
        }

        return null;
    }

    public RaceState startRace(List<Integer> racingIds) {
        Race race = new Race(racingIds);
        this.races.put(race.getId(), race);
        return race.buildState(this.serverInfo);
    }

}
