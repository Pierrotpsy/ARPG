package ch.epfl.cs107.play.game.arpg;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.area.Castle;
import ch.epfl.cs107.play.game.arpg.area.CastleRoad;
import ch.epfl.cs107.play.game.arpg.area.Cave;
import ch.epfl.cs107.play.game.arpg.area.Farm;
import ch.epfl.cs107.play.game.arpg.area.Road;
import ch.epfl.cs107.play.game.arpg.area.Temple;
import ch.epfl.cs107.play.game.arpg.area.TempleRoad;
import ch.epfl.cs107.play.game.arpg.area.Village;
import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class ARPG extends RPG {
	public final static float CAMERA_SCALE_FACTOR = 13.f;
	public final static float STEP = 0.05f;


	/**
	 * Add all the areas
	 */
	private void createAreas(){

		addArea(new Farm());
		addArea(new Village());
		addArea(new Road());
		addArea(new CastleRoad());
		addArea(new Castle());
		addArea(new TempleRoad());
		addArea(new Temple());
		addArea(new Cave());

	}

	@Override
	public boolean begin(Window window, FileSystem fileSystem) {

		if (super.begin(window, fileSystem)) {

			createAreas();
			setCurrentArea("zelda/Ferme", true);
			initPlayer(new ARPGPlayer(getCurrentArea(), Orientation.DOWN, new DiscreteCoordinates(6, 10)));
			return true;
		}
		return false;
	}



	@Override
	public String getTitle() {
		return "ARPG";
	}

}

