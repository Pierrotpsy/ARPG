package ch.epfl.cs107.play.game.arpg.area;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.immobile.CastleDoor;
import ch.epfl.cs107.play.game.arpg.actor.mobs.Bomber;
import ch.epfl.cs107.play.game.arpg.actor.mobs.DarkLord;
import ch.epfl.cs107.play.game.arpg.actor.mobs.FireSpell;
import ch.epfl.cs107.play.game.arpg.actor.mobs.FlameSkull;
import ch.epfl.cs107.play.game.arpg.actor.mobs.LogMonster;
import ch.epfl.cs107.play.game.arpg.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.arpg.actor.projectiles.MagicWaterProjectile;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.signal.logic.Logic;

public class CastleRoad extends ARPGArea implements Spawnable {
	private int spawnCooldown = SPAWN_COOLDOWN + 200;
	
	@Override
	public String getTitle() {
		return "zelda/RouteChateau";
	}

	@Override
	protected void createArea() {
		registerActor(new Background(this));
        registerActor(new Foreground(this));
        registerActor(new Door("zelda/Route", new DiscreteCoordinates(9,18), Logic.TRUE ,this , Orientation.DOWN, new DiscreteCoordinates(9,0), new DiscreteCoordinates(10,0)));
        registerActor(new CastleDoor("zelda/Chateau", new DiscreteCoordinates(7,1), Logic.FALSE ,this , Orientation.UP, new DiscreteCoordinates(9,13), new DiscreteCoordinates(10,13)));
        registerActor(new DarkLord(this, Orientation.DOWN, new DiscreteCoordinates(9,10)));
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
			spawnCooldown = SPAWN_COOLDOWN + 200;
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
