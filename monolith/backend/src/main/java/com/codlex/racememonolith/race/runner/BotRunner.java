package com.codlex.racememonolith.race.runner;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BotRunner extends Runner {

    private static final ScheduledExecutorService SERVICE = Executors.newSingleThreadScheduledExecutor();
    private final double speed;

    public BotRunner(int id, String username) {
        super();
        this.id = id;
        this.username = username;
        this.speed = Math.random() * 0.002;
    }

    public void start() {
        SERVICE.scheduleAtFixedRate(() -> {
            addDistance(Math.random() * this.speed);
        }, 0, 1, TimeUnit.SECONDS);
    }
}