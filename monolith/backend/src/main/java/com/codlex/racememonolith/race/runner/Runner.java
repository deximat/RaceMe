package com.codlex.racememonolith.race.runner;

import com.codlex.racememonolith.race.Race;
import com.codlex.racememonolith.user.User;
import com.codlex.racememonolith.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Data
@Slf4j
public class Runner {
    private Race race;
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

            // TODO: fix adding rating
            // this.repository.findById(this.id).get().addRating(this.reward);
        }
    }


    public void attachToRace(Race race) {
        this.race = race;
    }

    public Runner(int id, String username, UserRepository repository) {
        this.id = id;
        this.username = username;
//        this.userRepository = repository;
    }

    public void quit() {
        this.isDNF = true;
        this.finishedAt = System.currentTimeMillis();
        this.reward = -10;
        if (!(this instanceof BotRunner)) {
//            TODO: fix adding rating
//            User user = this.userRepository.findById(this.id).get();
//            user.addRating(this.reward);
        }
    }

    public boolean isFinished() {
        return this.isFinished || this.isDNF;
    }
}