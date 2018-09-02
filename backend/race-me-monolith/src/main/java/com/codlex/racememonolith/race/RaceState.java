package com.codlex.racememonolith.race;

import java.util.List;

import com.codlex.racememonolith.race.Race.Runner;
import lombok.Data;

@Data
public class RaceState {

	public enum RaceStatus {
		Running, Waiting, Done;
	}

	final int id;
	final List<Runner> runners;
	final RaceStatus status;

	public static final RaceState WAITING = new RaceState(-1, null, RaceStatus.Waiting);
}