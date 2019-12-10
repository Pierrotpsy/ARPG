package ch.epfl.cs107.play.game.arpg.actor.mobs;

import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

public class ARPGMobs extends Player{

	public ARPGMobs(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
		super(area, orientation, coordinates);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean wantsCellInteraction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean wantsViewInteraction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void interactWith(Interactable other) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean takeCellSpace() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCellInteractable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isViewInteractable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		
	}
	
	private class ARPGMobHandler implements ARPGInteractionVisitor {
		
	}

}
