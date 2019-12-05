package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.arpg.area.ARPGArea;

/**
 * Specific area
 */
public class Farm extends ARPGArea {
	
	@Override
	public String getTitle() {
		return "zelda/Ferme";
	}

	protected void createArea() {
        // Base
        registerActor(new Background(this));
        registerActor(new Foreground(this));
	}
	
}

