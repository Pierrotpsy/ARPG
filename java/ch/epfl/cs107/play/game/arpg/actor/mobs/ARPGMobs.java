package ch.epfl.cs107.play.game.arpg.actor.mobs;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;

public class ARPGMobs extends Player{
	private final static int MAX_HP = 100;
	private final static double PROBABILITY_TO_MOVE = 0.6;
	private int hp;
	private boolean isCellSpaceTaken = true;
	private ARPGMobHandler handler = new ARPGMobHandler();
	
	public ARPGMobs(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
		super(area, orientation, coordinates);
		hp = MAX_HP;
	}

	
	@Override
	public void update(float deltaTime) {

		super.update(deltaTime);
	}
	
	//Kills a mob
	public void kill() {
	}
	
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return null;
	}

	@Override
	public boolean wantsCellInteraction() {
		return false;
	}

	@Override
	public boolean wantsViewInteraction() {
		return false;
	}

	@Override
	public void interactWith(Interactable other) {		
	}

	@Override
	public boolean takeCellSpace() {
		return isCellSpaceTaken;
	} 

	@Override
	public boolean isCellInteractable() {
		return false;
	}

	@Override
	public boolean isViewInteractable() {
		return false;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		
	}

	@Override
	public void draw(Canvas canvas) {
		
	}
	
	//Methods for vulnerability to a certain type of damage
	public boolean isVulnerableFire() {
		return false;
	}
	
	public boolean isVulnerablePhysical() {
		return false;
	}

	public boolean isVulnerableMagic() {
		return false;
	}
	
	//Damages a mob by a given amount
	public void damage(int dmg) {		
	}
	
	//Moves a mob
	public void move() {	
	}
	
	/**
     * Orientate or Move this player in the given orientation if the given button is down
     * @param orientation (Orientation): given orientation, not null
     * @param b (Button): button corresponding to the given orientation, not null
     */
    public void moveOrientate(Orientation orientation, Button b){
    }
    
    //Handler for mobs
	public class ARPGMobHandler implements ARPGInteractionVisitor {
		
	}

	

}
