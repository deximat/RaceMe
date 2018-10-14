package com.codlex.raceme.api;

import com.codlex.raceme.data.RaceState;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("race-service")
public interface RaceService {

    @RequestMapping(method = RequestMethod.GET, value = "/find/race")
    RaceState findRaceByUserId( int userId);

    @RequestMapping(method = RequestMethod.GET, value = "/find/race")
    RaceState startRace(List<Integer> racingIds);
//    RaceState startRace(List<Integer> racingIds);
}
