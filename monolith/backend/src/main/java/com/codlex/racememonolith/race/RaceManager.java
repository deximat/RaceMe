package com.codlex.racememonolith.race;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

import com.codlex.racememonolith.user.UserService;
import com.codlex.racememonolith.race.runner.Runner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/race-service/")
public class RaceManager {

    @Data
    @AllArgsConstructor
    @ToString
    public static class Server {
        String address;
        int port;

        public static final Server NONE = null;
    }

    private Map<Integer, Race> races = new HashMap<>();

    private Server serverInfo;

    @Autowired
    private UserService userService;

    public RaceManager(final Environment environment) {
//        try {
            this.serverInfo = new Server("192.168.1.91", 8080);
            log.debug("Server info: " + this.serverInfo);
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
        this.userService.addRating(runner.getId(), 111);
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
