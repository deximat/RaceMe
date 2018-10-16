package com.codlex.raceme.raceservice;


import com.codlex.raceme.api.LoginService;
import com.codlex.raceme.data.RaceState;
import com.codlex.raceme.data.Runner;
import com.codlex.raceme.data.Server;
import com.codlex.raceme.raceservice.runner.BotRunner;
import com.google.common.collect.ComparisonChain;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Race {

    public static final int RACERS_COUNT = 5;

    public Runner findRunner(Integer userId) {
        return this.runners.get(userId);
    }

    private static final AtomicInteger ID_GENERATOR = new AtomicInteger();

    @Getter
    final int id;

    @Getter
    private final long startedAt;

    final Map<Integer, Runner> runners;

    public Race(final List<Integer> userIds, LoginService loginService) {
        this.id = ID_GENERATOR.incrementAndGet();
        this.runners = buildRunners(userIds, loginService);
        this.startedAt = System.currentTimeMillis();
    }

    private Map<Integer, Runner> buildRunners(List<Integer> userIds, LoginService loginService) {
        Map<Integer, Runner> runners = new HashMap<>();
        for (Integer userId : userIds) {
            runners.put(userId, new Runner(userId, loginService.getUsername(userId)));
        }

        BotRunner bot1 = new BotRunner(-101, "Bolt");
        bot1.start();
        runners.put(bot1.getId(), bot1);

        BotRunner bot2 = new BotRunner(-102, "Petar");
        bot2.start();
        runners.put(bot2.getId(), bot2);

        BotRunner bot3 = new BotRunner(-103, "John");
        bot3.start();
        runners.put(bot3.getId(), bot3);

        BotRunner bot4 = new BotRunner(-104, "Mitrovic");
        // bot4.start();
        bot4.setDNF(true);
        runners.put(bot4.getId(), bot4);

        return runners;
    }


    public RaceState buildState(Server serverInfo) {
        return new RaceState(this.id, new ArrayList(getSortedRunners()), this.startedAt, RaceState.RaceStatus.Running, serverInfo);
    }


    private List<Runner> getSortedRunners() {
        List<Runner> allRunners = new ArrayList<>(this.runners.values());
        allRunners.sort((runner1, runner2) -> {
            return ComparisonChain.start()
                    .compare(runner2.getDistance(), runner1.getDistance())
                    .compare(runner1.getFinishedAt(), runner2.getFinishedAt())
                    .result();
        });
        return allRunners;
    }

    public int getPosition(int id) {
        return getSortedRunners().indexOf(findRunner(id));
    }
}
