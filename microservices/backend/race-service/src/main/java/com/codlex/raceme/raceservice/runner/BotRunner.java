package com.codlex.raceme.raceservice.runner;


import com.codlex.raceme.data.Runner;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BotRunner extends Runner {

    private static final ScheduledExecutorService SERVICE = Executors.newSingleThreadScheduledExecutor();
    private final double speed;

    public BotRunner(int id, String username) {
        super(id, username);
        this.speed = Math.random() * 1;
    }

    public void start() {
        SERVICE.scheduleAtFixedRate(() -> {
            addDistance(5, Math.random() * this.speed);
        }, 0, 1, TimeUnit.SECONDS);
    }
}