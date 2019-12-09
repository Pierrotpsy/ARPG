package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class CastleDoor extends Door {

	private Logic signal;
	private boolean isCellSpaceTaken = true;
	private RPGSprite closedDoorSprite = new RPGSprite("zelda/castleDoor.close", 2, 2, this, new RegionOfInterest(0, 0, 32, 32));
	private RPGSprite openDoorSprite = new RPGSprite("zelda/castleDoor.open", 2, 2, this, new RegionOfInterest(0, 0, 32, 32));
	
	public CastleDoor(String destination, DiscreteCoordinates otherSideCoordinates, Logic signal, Area area, Orientation orientation, DiscreteCoordinates position, DiscreteCoordinates otherCells) {
		super(destination, otherSideCoordinates, signal, area, orientation, position, otherCells);
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
	
	public void closeDoor() {
		signal = Logic.FALSE;
		isCellSpaceTaken = true;
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
	
	public void openDoor() {
		signal = Logic.TRUE;
		isCellSpaceTaken = false;
	}
}
