package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.area.ARPGArea;
import ch.epfl.cs107.play.math.Vector;

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
        registerActor(new ARPGPlayer(new Vector(20, 10), "ghost.2"));
        }
	
	
    
}
