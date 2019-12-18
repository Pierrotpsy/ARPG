package ch.epfl.cs107.play.game.arpg.actor.immobile;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class CaveDoor extends Door {

	private Logic signal;
	private boolean isCellSpaceTaken = true;
	private RPGSprite closedDoorSprite = new RPGSprite("zelda/cave.close", 1, 1, this, new RegionOfInterest(0, 0, 16, 16));
	private RPGSprite openDoorSprite = new RPGSprite("zelda/cave.open", 1, 1, this, new RegionOfInterest(0, 0, 16, 16));
	
	public CaveDoor(String destination, DiscreteCoordinates otherSideCoordinates, Logic signal, Area area, Orientation orientation, DiscreteCoordinates position) {
		super(destination, otherSideCoordinates, signal, area, orientation, position);
		this.signal = signal;
	}

    @Override
    public boolean isCellInteractable() {
        return signal == null || signal.isOn();
    }
	
	@Override
    public boolean isViewInteractable(){
        return true;
    }
	
	@Override
	public boolean isOpen() {
    	return signal.isOn();
    }
	
	@Override
    public boolean takeCellSpace() {
        return isCellSpaceTaken;
    }
	
	@Override
	public void draw(Canvas canvas) {
		if (signal == Logic.FALSE) {
			closedDoorSprite.draw(canvas);
		}
		
		if (signal == Logic.TRUE) {
			openDoorSprite.draw(canvas);
		}
	}
	
	//Opens the door
	public void openDoor() {
		signal = Logic.TRUE;
		isCellSpaceTaken = false;
	}
}