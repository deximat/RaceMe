package com.codlex.racememonolith.race.runner;

import com.codlex.racememonolith.login.UserManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Data
@Slf4j
public class Runner {
    protected int id;
    protected String username;
    private volatile double distance;
    private boolean isFinished;
    private long finishedAt = System.currentTimeMillis();
    private boolean isDNF;

    public synchronized void addDistance(double distance) {
        if (isFinished()) {
            return;
        }

        double targetDistance = 5;
        this.distance = Math.min(targetDistance, this.distance + distance);
        this.isFinished = this.distance >= targetDistance;

        if (!this.isFinished) {
            this.finishedAt = System.currentTimeMillis();
        }
    }

    public Runner() {
    }

    public Runner(int id) {
        this.id = id;
        this.username = UserManager.get().findById(id).getUsername();
    }

    public void quit() {
        this.isDNF = true;
        this.finishedAt = System.currentTimeMillis();
    }

    public boolean isFinished() {
        return this.isFinished || this.isDNF;
    }
}