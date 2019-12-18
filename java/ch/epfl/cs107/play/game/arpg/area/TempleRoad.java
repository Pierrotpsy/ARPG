package ch.epfl.cs107.play.game.arpg.area;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.Grass;
import ch.epfl.cs107.play.game.arpg.actor.immobile.Bridge;
import ch.epfl.cs107.play.game.arpg.actor.immobile.Rock;
import ch.epfl.cs107.play.game.arpg.actor.mobs.Bomber;
import ch.epfl.cs107.play.game.arpg.actor.mobs.FireSpell;
import ch.epfl.cs107.play.game.arpg.actor.mobs.FlameSkull;
import ch.epfl.cs107.play.game.arpg.actor.mobs.LogMonster;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.signal.logic.Logic;

public class TempleRoad extends ARPGArea implements Spawnable {
	
	private int spawnCooldown = SPAWN_COOLDOWN;

	@Override
	public String getTitle() {
		return "zelda/RouteTemple";
	}

	
	
	protected void createArea() {
        // Base
        registerActor(new Background(this));
        registerActor(new Foreground(this));
        registerActor(new Door("zelda/Route", new DiscreteCoordinates(18,10), Logic.TRUE ,this , Orientation.LEFT, new DiscreteCoordinates(0,4), new DiscreteCoordinates(0,5), new DiscreteCoordinates(0,6)));
        registerActor(new Door("zelda/Temple", new DiscreteCoordinates(4,1), Logic.TRUE ,this , Orientation.UP, new DiscreteCoordinates(5,6)));       
	
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
	
