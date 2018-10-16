package com.codlex.raceme.raceservice;

import com.codlex.raceme.api.LoginService;
import com.codlex.raceme.data.RaceState;
import com.codlex.raceme.data.Runner;
import com.codlex.raceme.data.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
    public RaceState quit(@PathVariable("raceId") Integer raceId, @PathVariable("userId") Integer userId) {
        log.debug("##### QUIT raceId: {}, userId: {}, wants to quit: {}", raceId, userId);

        final Race race = this.races.get(raceId);
        final Runner runner = race.findRunner(userId);
        runner.quit(this.loginService);

        return race.buildState(this.serverInfo);
    }

    @RequestMapping(value = "/race/{raceId}/user/{userId}/distance/{distance}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public RaceState updateDistance(@PathVariable("raceId") Integer raceId, @PathVariable("userId") Integer userId,
                                    @PathVariable(value = "distance", required = false) Double distance) {
        log.debug("##### DISTANCE_UPDATE raceId: {}, userId: {}, distance: {}", raceId, userId, distance);
        int targetDistance = 5;

        final Race race = this.races.get(raceId);
        final Runner runner = race.findRunner(userId);
        runner.addDistance(targetDistance, distance);

        boolean newIsFinished = runner.getDistance() >= targetDistance;
        boolean justFinished = !runner.isFinished() && newIsFinished;
        runner.setFinished(newIsFinished);

        if (!runner.isFinished()) {
            runner.setFinishedAt(System.currentTimeMillis());
        }

        if (justFinished) {
            runner.setReward(Race.RACERS_COUNT - race.getPosition(runner.getId()) * 10);
            this.loginService.addRating(runner.getId(), runner.getReward());
        }

        return race.buildState(this.serverInfo);
    }

    @RequestMapping(value = "/race/find/user/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public RaceState findRaceByUserId(@PathVariable("userId") int userId) {

        for (Race race : this.races.values()) {
            Runner runner = race.findRunner(userId);
            if (runner != null && !runner.isFinished()) {
                return race.buildState(this.serverInfo);
            }
        }

        return RaceState.NONE;
    }

    @RequestMapping(value = "/race/start/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public RaceState startRace(@RequestBody List<Integer> racingIds) {

        log.debug("##### START_RACE racers: {}", racingIds);

        Race race = new Race(racingIds);
        this.races.put(race.getId(), race);
        return race.buildState(this.serverInfo);
    }

}
