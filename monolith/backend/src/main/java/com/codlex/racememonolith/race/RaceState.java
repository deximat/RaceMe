package com.codlex.racememonolith.race;

import java.util.List;

import com.codlex.racememonolith.race.runner.Runner;
import lombok.Data;

@Data
public class RaceState {

	public enum RaceStatus {
		Running, Waiting, Done;
	}

	final int id;
	final List<Runner> runners;
	final long startedAt;
	final RaceStatus status;
	final RaceManager.Server server;

	public static final RaceState WAITING = new RaceState(-1, null, 0, RaceStatus.Waiting, RaceManager.Server.NONE);


}