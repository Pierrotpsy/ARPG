package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.CastleDoor;
import ch.epfl.cs107.play.game.arpg.actor.mobs.LogMonster;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class CastleRoad extends ARPGArea {

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "zelda/RouteChateau";
	}

	@Override
	protected void createArea() {
		registerActor(new Background(this));
        registerActor(new Foreground(this));
        registerActor(new Door("zelda/Route", new DiscreteCoordinates(9,18), Logic.TRUE ,this , Orientation.DOWN, new DiscreteCoordinates(9,0), new DiscreteCoordinates(10,0)));
        registerActor(new CastleDoor("zelda/Chateau", new DiscreteCoordinates(7,1), Logic.FALSE ,this , Orientation.UP, new DiscreteCoordinates(9,13), new DiscreteCoordinates(10,13)));
        registerActor(new LogMonster(this, Orientation.DOWN, new DiscreteCoordinates(9,9)));
	}

}
