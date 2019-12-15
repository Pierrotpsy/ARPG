package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.Bombs;
import ch.epfl.cs107.play.game.arpg.actor.Grass;
import ch.epfl.cs107.play.game.arpg.actor.mobs.FlameSkull;
import ch.epfl.cs107.play.game.arpg.area.ARPGArea;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Road extends ARPGArea {
	private int i, j;
	
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
        registerActor(new Bombs(this, new DiscreteCoordinates(6, 12), 100));
        for (i = 5; i < 8; i++) {
        	for (j = 6; j < 12; j++) {
        		registerActor(new Grass(this, new DiscreteCoordinates(i, j)));
        	}
        }
        registerActor(new FlameSkull(this, Orientation.RIGHT, new DiscreteCoordinates(8,10)));
	}
}
