package com.codlex.racememonolith.race.runner;


import com.codlex.racememonolith.race.Race;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BotRunner extends Runner {

    private static final ScheduledExecutorService SERVICE = Executors.newSingleThreadScheduledExecutor();
    private final double speed;

    public BotRunner(int id, String username, Race race) {
        super(id, race, false);
        this.username = username;
        this.speed = Math.random() * 1;
    }

    public void start() {
        SERVICE.scheduleAtFixedRate(() -> {
            addDistance(Math.random() * this.speed);
        }, 0, 1, TimeUnit.SECONDS);
    }
}