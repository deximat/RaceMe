package com.codlex.racememonolith.race;

import com.codlex.racememonolith.race.RaceState.RaceStatus;
import com.codlex.racememonolith.race.runner.BotRunner;
import com.codlex.racememonolith.race.runner.Runner;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Race {


    public Runner findRunner(Integer userId) {
        return this.runners.get(userId);
    }


    private static final AtomicInteger ID_GENERATOR = new AtomicInteger();

    @Getter
    final int id;

    final Map<Integer, Runner> runners;

    @Getter
    private final long startedAt;

    public Race(final List<Integer> userIds) {
        this.id = ID_GENERATOR.incrementAndGet();
        this.runners = buildRunners(userIds);
        this.startedAt = System.currentTimeMillis();
    }

    private Map<Integer, Runner> buildRunners(List<Integer> userIds) {
        Map<Integer, Runner> runners = new HashMap<>();
        for (Integer userId : userIds) {
            runners.put(userId, new Runner(userId));
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
        bot4.start();
        runners.put(bot4.getId(), bot4);

        return runners;
    }


    public RaceState buildState() {
        return new RaceState(this.id, new ArrayList(this.runners.values()), this.startedAt, RaceStatus.Running);
    }

}
