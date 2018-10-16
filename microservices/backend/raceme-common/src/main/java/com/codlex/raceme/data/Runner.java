package com.codlex.raceme.data;

import com.codlex.raceme.api.LoginService;
import com.codlex.raceme.api.RaceService;
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


    public synchronized void addDistance(double targetDistance, double distance) {
        if (isFinished()) {
            return;
        }
        this.distance = Math.min(targetDistance, this.distance + distance);
    }

    public Runner(int id,  String username) {
        this.id = id;
        this.username = username;
    }

    public void quit(LoginService loginService) {
        this.isDNF = true;
        this.finishedAt = System.currentTimeMillis();
        this.reward = -10;

        loginService.addRating(getId(), getReward());
    }

    public boolean isFinished() {
        return this.isFinished || this.isDNF;
    }
}