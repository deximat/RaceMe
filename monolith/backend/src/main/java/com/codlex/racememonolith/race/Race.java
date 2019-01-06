package com.codlex.racememonolith.race;

import com.codlex.racememonolith.race.RaceState.RaceStatus;
import com.codlex.racememonolith.race.runner.BotRunner;
import com.codlex.racememonolith.race.runner.Runner;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import lombok.Getter;
import org.springframework.util.comparator.Comparators;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
    @Id
    @GeneratedValue
    final int id;

    @Getter
    private final long startedAt;

    final Map<Integer, Runner> runners;

    public Race(final List<Runner> userIds) {
        this.id = ID_GENERATOR.incrementAndGet();
        this.runners = buildRunners(userIds);
        this.startedAt = System.currentTimeMillis();
    }

    private Map<Integer, Runner> buildRunners(List<Runner> humanRunners) {
        Map<Integer, Runner> runners = new HashMap<>();

        for (Runner runner : humanRunners) {
            runner.attachToRace(this);
            runners.put(runner.getId(), runner);
        }

        BotRunner bot1 = new BotRunner(-101, "Bolt", this, 0.05);
        bot1.start();
        runners.put(bot1.getId(), bot1);

        BotRunner bot2 = new BotRunner(-102, "Petar", this, 0.03);
        bot2.start();
        runners.put(bot2.getId(), bot2);

        BotRunner bot3 = new BotRunner(-103, "John", this, 0.02);
        bot3.start();
        runners.put(bot3.getId(), bot3);

        BotRunner bot4 = new BotRunner(-104, "Mitrovic", this, 0.01);
        // bot4.start();
        bot4.setDNF(true);
        runners.put(bot4.getId(), bot4);

        return runners;
    }


    public RaceState buildState(RaceManager.Server serverInfo) {
        return new RaceState(this.id, new ArrayList(getSortedRunners()), this.startedAt, RaceStatus.Running, serverInfo);
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
