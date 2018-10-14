package com.codlex.raceme.data;

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
//            this.reward = (Race.RACERS_COUNT - this.race.getPosition(this.id)) * 10;

        }
    }

    public Runner(int id,  boolean getUsername) {
        this.id = id;
        if (getUsername) {
            // TODO: fix username;
            this.username = "FIX"; // this.repository.findById(id).get().getUsername();
        }
    }

    public void quit() {
        this.isDNF = true;
        this.finishedAt = System.currentTimeMillis();
        this.reward = -10;
        // TODO: fix adding rating
        // this.repository.findById(this.id).get().addRating(this.reward);
    }

    public boolean isFinished() {
        return this.isFinished || this.isDNF;
    }
}