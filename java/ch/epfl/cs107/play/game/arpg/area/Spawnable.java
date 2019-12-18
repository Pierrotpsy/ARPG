package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.math.DiscreteCoordinates;

//Used to make an area spawnable 
interface Spawnable {
	static final double SPAWN_RATE = 0.3;
	static final double SPAWN_RATE_MOB = 0.5;
	static final int SPAWN_COOLDOWN = 75;
	
	public void spawn();
	
	public DiscreteCoordinates randomSpawnPoint();
}
