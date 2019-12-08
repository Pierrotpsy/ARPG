package ch.epfl.cs107.play.game.arpg;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.area.Castle;
import ch.epfl.cs107.play.game.arpg.area.CastleRoad;
import ch.epfl.cs107.play.game.arpg.area.Farm;
import ch.epfl.cs107.play.game.arpg.area.Road;
import ch.epfl.cs107.play.game.arpg.area.Village;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.window.Window;

public class ARPG extends AreaGame {
	public final static float CAMERA_SCALE_FACTOR = 13.f;
	public final static float STEP = 0.05f;


	private ARPGPlayer player;
	private final String[] areas = {"zelda/Ferme", "zelda/Village", "zelda/Route"};
	private final DiscreteCoordinates startingPosition = new DiscreteCoordinates(5,15);

	private int areaIndex;
	/**
	 * Add all the areas
	 */
	private void createAreas(){

		addArea(new Farm());
		addArea(new Village());
		addArea(new Road());
		addArea(new CastleRoad());
		addArea(new Castle());

	}

	@Override
	public boolean begin(Window window, FileSystem fileSystem) {


		if (super.begin(window, fileSystem)) {

			createAreas();
			areaIndex = 2;
			Area area = setCurrentArea(areas[areaIndex], true);
			player = new ARPGPlayer(area, Orientation.DOWN, startingPosition,"ghost.1");
			area.registerActor(player);
			area.setViewCandidate(player);
			return true;
		}
		return false;
	}

	@Override
	public void update(float deltaTime) {
		
		if(player.isPassingADoor()){
            Door door = player.passedDoor();
            player.leaveArea();
            Area area = setCurrentArea(door.getDestination(), false);
            player.enterArea(area, door.getOtherSideCoordinates());
            System.out.println(area);
        }	
		
		super.update(deltaTime);

	}

	@Override
	public void end() {
	}

	@Override
	public String getTitle() {
		return "ARPG";
	}

}

