package ch.epfl.cs107.play.game.arpg.area;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.collectables.CastleKey;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Coin;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Heart;
import ch.epfl.cs107.play.game.arpg.actor.mobs.Bomber;
import ch.epfl.cs107.play.game.arpg.actor.mobs.FireSpell;
import ch.epfl.cs107.play.game.arpg.actor.mobs.FlameSkull;
import ch.epfl.cs107.play.game.arpg.actor.mobs.LogMonster;
import ch.epfl.cs107.play.game.arpg.area.ARPGArea;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.signal.logic.Logic;

/**
 * Specific area
 */
public class Farm extends ARPGArea implements Spawnable {
	private int spawnCooldown = SPAWN_COOLDOWN ;

	@Override
	public String getTitle() {
		return "zelda/Ferme";
	}

			
	protected void createArea() {
        // Base
        registerActor(new Background(this));
        registerActor(new Foreground(this));
        registerActor(new Door("zelda/Route", new DiscreteCoordinates(1,15), Logic.TRUE ,this , Orientation.RIGHT, new DiscreteCoordinates(19,15), new DiscreteCoordinates(19,16)));
        registerActor(new Door("zelda/Village", new DiscreteCoordinates(4,18), Logic.TRUE ,this , Orientation.DOWN, new DiscreteCoordinates(4,0), new DiscreteCoordinates(5,0)));
        registerActor(new Door("zelda/Village", new DiscreteCoordinates(14,18), Logic.TRUE ,this , Orientation.DOWN, new DiscreteCoordinates(13,0), new DiscreteCoordinates(14,0)));
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