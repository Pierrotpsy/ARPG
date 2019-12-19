package ch.epfl.cs107.play.game.arpg.area;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.immobile.Bombs;
import ch.epfl.cs107.play.game.arpg.actor.immobile.Bridge;
import ch.epfl.cs107.play.game.arpg.actor.immobile.Grass;
import ch.epfl.cs107.play.game.arpg.actor.immobile.Rock;
import ch.epfl.cs107.play.game.arpg.actor.mobs.Bomber;
import ch.epfl.cs107.play.game.arpg.actor.mobs.FireSpell;
import ch.epfl.cs107.play.game.arpg.actor.mobs.FlameSkull;
import ch.epfl.cs107.play.game.arpg.actor.mobs.LogMonster;
import ch.epfl.cs107.play.game.arpg.area.ARPGArea;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Road extends ARPGArea implements Spawnable {
	private int i, j;
	private int spawnCooldown = SPAWN_COOLDOWN;
	
	@Override
	public String getTitle() {
		return "zelda/Route";
	}

	
	
	protected void createArea() {
        // Base
        registerActor(new Background(this));
        registerActor(new Foreground(this));
        registerActor(new Door("zelda/Ferme", new DiscreteCoordinates(18,15), Logic.TRUE ,this , Orientation.UP, new DiscreteCoordinates(0,15), new DiscreteCoordinates(0,16)));
        registerActor(new Door("zelda/Village", new DiscreteCoordinates(29,18), Logic.TRUE ,this , Orientation.DOWN, new DiscreteCoordinates(9,0), new DiscreteCoordinates(10,0)));
        registerActor(new Door("zelda/RouteChateau", new DiscreteCoordinates(9,1), Logic.TRUE ,this , Orientation.UP, new DiscreteCoordinates(9,19), new DiscreteCoordinates(10,19)));
        registerActor(new Door("zelda/RouteTemple", new DiscreteCoordinates(1,6), Logic.TRUE ,this , Orientation.RIGHT, new DiscreteCoordinates(19,9), new DiscreteCoordinates(19,10), new DiscreteCoordinates(19,11)));

        for (i = 5; i < 8; i++) {
        	for (j = 6; j < 12; j++) {
        		registerActor(new Grass(this, new DiscreteCoordinates(i, j)));
        	}
        }
        registerActor(new Rock(this, new DiscreteCoordinates(15, 8)));
        registerActor(new Rock(this, new DiscreteCoordinates(15, 9)));
        registerActor(new Rock(this, new DiscreteCoordinates(15, 10)));
        registerActor(new Rock(this, new DiscreteCoordinates(15, 11)));
        registerActor(new Rock(this, new DiscreteCoordinates(15, 12)));
        registerActor(new Bridge(this, new DiscreteCoordinates(16, 10)));
	}
	

	
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (spawnCooldown > 0) {
			spawnCooldown--;
		} else {
			spawn();
		}		
	}

	@Override
	public void spawn() {
		double spawnRate = RandomGenerator.getInstance().nextDouble();
		double mobRate = RandomGenerator.getInstance().nextDouble();
		if (spawnRate < SPAWN_RATE) {
			DiscreteCoordinates coord = randomSpawnPoint();
			if (mobRate < 0.33 && coord != null) {
				registerActor(new LogMonster(this, Orientation.DOWN, randomSpawnPoint()));
			} else if (mobRate < 0.66 && coord != null){
				registerActor(new Bomber(this, Orientation.DOWN, randomSpawnPoint()));
			} else if (coord != null){
				registerActor(new FlameSkull(this, Orientation.DOWN, randomSpawnPoint()));
			}
			spawnCooldown = SPAWN_COOLDOWN;
		}
	}

	@Override
	public DiscreteCoordinates randomSpawnPoint() {
		List<DiscreteCoordinates> area = new DiscreteCoordinates(0,0).getArea(getWidth(), getHeight());
		Collections.shuffle(area);
		for (DiscreteCoordinates coord : area) {
			if (registerActor(new FireSpell(this, Orientation.DOWN, coord, 0))) {
				return coord;
			}
		}
		return null;
	}
}
