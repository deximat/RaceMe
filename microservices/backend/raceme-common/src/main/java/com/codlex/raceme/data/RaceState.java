package com.codlex.raceme.data;

import lombok.Data;

import java.util.List;

@Data
public class RaceState {

	public enum RaceStatus {
		Running, Waiting, Done;
	}

	final int id;
	final List<Runner> runners;
	final long startedAt;
	final RaceStatus status;
	final Server server;

	public static final RaceState WAITING = new RaceState(-1, null, 0, RaceStatus.Waiting, Server.NONE);


}