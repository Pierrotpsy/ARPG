package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Bow;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Staff;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Cave extends ARPGArea{
	@Override
	public String getTitle() {
		return "GrotteMew";
	}

	
	
	protected void createArea() {
        // Base
        registerActor(new Background(this));
        registerActor(new Door("zelda/Village", new DiscreteCoordinates(25,17), Logic.TRUE ,this , Orientation.UP, new DiscreteCoordinates(8,2)));
        registerActor(new Bow(this, new DiscreteCoordinates(8,7)));
	}
}
