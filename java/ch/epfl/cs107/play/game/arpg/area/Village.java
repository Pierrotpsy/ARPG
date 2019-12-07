package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.area.ARPGArea;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;

/**
 * Specific area
 */
public class Village extends ARPGArea {
	
	@Override
	public String getTitle() {
		return "zelda/Village";
	}
	

	protected void createArea() {
        // Base
	
        registerActor(new Background(this)) ;
        registerActor(new Foreground(this)) ;
        registerActor(new Door("zelda/Ferme", new DiscreteCoordinates(4,1), Logic.TRUE ,this , Orientation.UP, new DiscreteCoordinates(4,19), new DiscreteCoordinates(5,19)));
        registerActor(new Door("zelda/Ferme", new DiscreteCoordinates(14,1), Logic.TRUE ,this , Orientation.UP, new DiscreteCoordinates(13,19), new DiscreteCoordinates(14,19), new DiscreteCoordinates(15,19)));
        registerActor(new Door("zelda/Route", new DiscreteCoordinates(9,1), Logic.TRUE ,this , Orientation.UP, new DiscreteCoordinates(29,19), new DiscreteCoordinates(30,19)));
        }
	
	
    
}
