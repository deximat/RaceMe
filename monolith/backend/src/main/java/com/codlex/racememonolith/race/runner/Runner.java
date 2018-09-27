package com.codlex.racememonolith.race.runner;

import com.codlex.racememonolith.login.UserManager;
import com.codlex.racememonolith.race.Race;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Data
@Slf4j
public class Runner {
    private final Race race;
    protected int id;
    protected String username;
    private volatile double distance;
    private boolean isFinished;
    private long finishedAt = System.currentTimeMillis();
    private boolean isDNF;

    private int reward;

    public synchronized void addDistance(double distance) {
        if (isFinished()) {
            return;
        }

        double targetDistance = 5;
        this.distance = Math.min(targetDistance, this.distance + distance);

        boolean newIsFinished = this.distance >= targetDistance;
        boolean justFinished = !this.isFinished && newIsFinished;
        this.isFinished = newIsFinished;

        if (!this.isFinished) {
            this.finishedAt = System.currentTimeMillis();
        }

        if (justFinished) {
            this.reward = (Race.RACERS_COUNT - this.race.getPosition(this.id)) * 10;
            UserManager.get().findById(this.id).addRating(this.reward);
        }
    }

    public Runner(int id, Race race, boolean getUsername) {
        this.id = id;
        this.race = race;
        if (getUsername) {
            this.username = UserManager.get().findById(id).getUsername();
        }
    }

    public void quit() {
        this.isDNF = true;
        this.finishedAt = System.currentTimeMillis();
        this.reward = -10;
        UserManager.get().findById(this.id).addRating(this.reward);
    }

    public boolean isFinished() {
        return this.isFinished || this.isDNF;
    }
}