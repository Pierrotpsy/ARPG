package ch.epfl.cs107.play.game.arpg.actor;


import java.awt.Color;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.ARPG;
import ch.epfl.cs107.play.game.arpg.area.ARPGArea;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public class ARPGPlayer extends Player {
	
	private float hp;
	private int i;
	private TextGraphics message;
	private Sprite sprite;
	private boolean isPassingADoor;
	private Door passedDoor;
	/// Animation duration in frame number
    private final static int ANIMATION_DURATION = 8;
    private final ARPGPlayerHandler handler = new ARPGPlayerHandler();
    
    Sprite[][] sprites = RPGSprite.extractSprites("zelda/player", 4, 1, 2, this, 16, 32, new Orientation[] {Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT});

    Animation[] animations = RPGSprite.createAnimations(ANIMATION_DURATION/2, sprites);
    
	/**
	 * Demo actor
	 * 
	 */
	public ARPGPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String spriteName) {
		super(owner, orientation, coordinates);
		this.hp = 10;
		message = new TextGraphics(Integer.toString((int)hp), 0.4f, Color.BLUE);
		message.setParent(this);
		message.setAnchor(new Vector(-0.3f, 0.1f));
		sprite = sprites[0][0];
		passedDoor = null;
		resetMotion();
	}
	 
	 @Override
	    public void update(float deltaTime) {
		 if (hp > 0) {
				hp -=deltaTime;
				message.setText(Integer.toString((int)hp));
			}
			if (hp < 0) hp = 0.f;
			Keyboard keyboard= getOwnerArea().getKeyboard();
	        moveOrientate(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
	        moveOrientate(Orientation.UP, keyboard.get(Keyboard.UP));
	        moveOrientate(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
	        moveOrientate(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
	        
	        switch(getOrientation()) {
	        	case LEFT :
	        		i = 3;
	        		break;
	        		
	        	case RIGHT :
	        		i =1;
	        		break;
	        		
	        	case UP :
	        		i = 0;
	        		break;
	        		
	        	case DOWN :
	        		i = 2;
	        		break;
	        		
	        	default :
	        		i = 2;
	        }
	        
	        if (isDisplacementOccurs()) {
		        animations[i].update(deltaTime);
	        } else animations[i].reset();
	       
	        
	        super.update(deltaTime);
	        
	        List<DiscreteCoordinates> coords = getCurrentCells();
	        if (coords != null) {
	        	for (DiscreteCoordinates c : coords) {
	        		if (((ARPGArea)getOwnerArea()).isDoor(c)) setIsPassingADoor(passedDoor);
	        	}
	        }
	    }

	    /**
	     * Orientate or Move this player in the given orientation if the given button is down
	     * @param orientation (Orientation): given orientation, not null
	     * @param b (Button): button corresponding to the given orientation, not null
	     */
	    private void moveOrientate(Orientation orientation, Button b){
	    
	        if(b.isDown()) {
	            if(getOrientation() == orientation) move(ANIMATION_DURATION);
	            else orientate(orientation);
	        }
	    }
	    /**
	     * Indicate the player just passed a door
	     *
	     */
	     @Override
	    protected void setIsPassingADoor(Door door){ // 
	    	passedDoor = door;
	        isPassingADoor = true;
	    }

	    /**@return (boolean): true if the player is passing a door*/
	    public boolean isPassingADoor(){
	        return isPassingADoor;
	    }
	 
	    public void resetDoorState() {
	    	isPassingADoor = false;
	    }
	    /**
	     * Leave an area by unregister this player
	     */
	    public void leaveArea(){
	        getOwnerArea().unregisterActor(this);
	    }

	    /**
	     *
	     * @param area (Area): initial area, not null
	     * @param position (DiscreteCoordinates): initial position, not null
	     */
	    public void enterArea(Area area, DiscreteCoordinates position){
	        area.registerActor(this);
	        area.setViewCandidate(this);
	        setOwnerArea(area);
	        setCurrentPosition(position.toVector());
	        resetDoorState();
	        resetMotion();
	        
	    }
    
	@Override
	public void draw(Canvas canvas) {
		animations[i].draw(canvas);	
		message.draw(canvas);
	}

	public boolean isWeak() {
		return (hp <= 0.f);
	}

	public void strengthen() {
		hp = 10;
	}

	///Ghost implements Interactable

	@Override
	public boolean takeCellSpace() {
		return true;
	}

	@Override
	public boolean isCellInteractable() {
		return true;
	}

	@Override
	public boolean isViewInteractable() {
		return true;
	}
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return Collections.singletonList (getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
	}

	@Override
	public boolean wantsCellInteraction() {

		return true;
	}

	@Override
	public boolean wantsViewInteraction() {
		Keyboard keyboard= getOwnerArea().getKeyboard();
		Button e = keyboard.get(Keyboard.E);
		if (e.isPressed()) {
			return true;
		}
		return false;
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);
	}

    public Door passedDoor(){
        return passedDoor;
    }
    
	private class ARPGPlayerHandler implements ARPGInteractionVisitor {
		@Override
		public void interactWith(Door door){
			setIsPassingADoor(door);
	    }
		
		@Override
		public void interactWith(Grass grass) {
			grass.slice();
		}

		
	}
}
