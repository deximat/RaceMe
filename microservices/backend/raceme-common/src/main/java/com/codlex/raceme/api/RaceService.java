package com.codlex.raceme.api;

import com.codlex.raceme.data.RaceState;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("race-service")
public interface RaceService {

    @RequestMapping(method = RequestMethod.GET, value = "/race/find/user/{userId}")
    RaceState findRaceByUserId(@PathVariable("userId") int userId);

    @RequestMapping(value = "/race/start/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    RaceState startRace(@RequestBody List<Integer> racingIds);

}
