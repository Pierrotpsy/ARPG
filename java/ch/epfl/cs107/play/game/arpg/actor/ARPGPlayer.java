package ch.epfl.cs107.play.game.arpg.actor;


import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.inventory.InventoryItem;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.ARPG;
import ch.epfl.cs107.play.game.arpg.area.ARPGArea;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.inventory.ARPGInventory;
import ch.epfl.cs107.play.game.arpg.inventory.ARPGItem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public class ARPGPlayer extends Player {
	
	private float hp;
	private int i;
	private TextGraphics message;
	private ShapeGraphics HPbarGreen;
	private ShapeGraphics HPbarRed;
	private Sprite sprite;
	private boolean isPassingADoor;
	private Door passedDoor;
	/// Animation duration in frame number
    private final static int ANIMATION_DURATION = 8;
    private final ARPGPlayerHandler handler = new ARPGPlayerHandler();
    private ARPGInventory inventory;
    private InventoryItem usedItem;
    private List<ARPGItem> keySet;
    
    Sprite[][] sprites = RPGSprite.extractSprites("zelda/player", 4, 1, 2, this, 16, 32, new Orientation[] {Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT});

    Animation[] animations = RPGSprite.createAnimations(ANIMATION_DURATION/2, sprites);
    
	/**
	 * Demo actor
	 * 
	 */
	public ARPGPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String spriteName) {
		super(owner, orientation, coordinates);
		this.hp = 100;
		message = new TextGraphics(Integer.toString((int)hp), 0.4f, Color.BLUE);
		HPbarGreen = new ShapeGraphics(new Polygon(new Vector(0.1f, 1.8f), new Vector((float) 1, 1.8f), new Vector((float) 1, 1.7f), new Vector( 0.1f, 1.7f)), Color.GREEN, Color.BLACK, 0.01f);
		HPbarGreen.setParent(this);
		message.setParent(this);
		message.setAnchor(new Vector(0.2f, 1.7f));
		sprite = sprites[0][0];
		passedDoor = null;
		inventory = new ARPGInventory(100, 100);
		keySet = new ArrayList<ARPGItem>(inventory.getItems().keySet());
		addItem(ARPGItem.Bomb, 3);
		addItem(ARPGItem.Bow, 1);
		addItem(ARPGItem.Staff, 1);
		addItem(ARPGItem.CastleKey, 1);
		addItem(ARPGItem.Sword, 1);
		
		for(Map.Entry<ARPGItem, Integer> entry: inventory.getItems().entrySet()) {
			if (entry.getValue() > 0) {
				usedItem = entry.getKey();
				break;
			}
		}

		resetMotion();
	}
	 
	 @Override
	    public void update(float deltaTime) {
		
		 
		 if (hp > 0) {
			 HPbarGreen = null;
			 HPbarRed = null;
			 HPbarGreen = new ShapeGraphics(new Polygon(new Vector(0f, 1.8f), new Vector((float) hp/100f, 1.8f), new Vector((float) hp/100f, 1.7f), new Vector( 0f, 1.7f)), Color.GREEN, Color.BLACK, 0.01f);
			 HPbarRed = new ShapeGraphics(new Polygon(new Vector((float) hp/100, 1.8f), new Vector(1f, 1.8f), new Vector(1f, 1.7f), new Vector((float) hp/100, 1.7f)), Color.RED, Color.BLACK, 0.01f);
			 HPbarGreen.setParent(this);
			 HPbarRed.setParent(this);
		 }
		 
		 
		 if (hp > 0) {
				message.setText(Integer.toString((int)hp));
				
			}
		 if (hp < 0) {
			 hp = 0.f;
			 HPbarGreen = null;
			 HPbarRed = null;
			 HPbarRed = new ShapeGraphics(new Polygon(new Vector(0f, 1.8f), new Vector(1f, 1.8f), new Vector(1f, 1.7f), new Vector(0f, 1.7f)), Color.RED, Color.BLACK, 0.01f);
			 HPbarRed.setParent(this);
		 }
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
	        
	        Button tab = keyboard.get(Keyboard.TAB);
			if (tab.isPressed()) {
				switchItem();
			}
			
			Button space= keyboard.get(Keyboard.SPACE);
			if (space.isPressed()) {
				useItem();
			}
			
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
		if (HPbarGreen != null) {
			HPbarGreen.draw(canvas);

		}
		if (HPbarRed != null) {
			HPbarRed.draw(canvas);

		}
		//message.draw(canvas);
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
    
    public boolean possess(InventoryItem item) {
    	return inventory.getItems().get(item) > 0;
    }
    
    public void damage(int d) {
    	hp -= d;
    }
    
    public boolean addItem(InventoryItem item, int amount) {
    	int i;
		if (inventory.getOverallWeight() + amount*item.getWeight() < inventory.getMaxWeight()) {
			int a = inventory.getItems().get(item);
			for (i = 0; i < amount - 1; i++) {
				inventory.getItems().replace((ARPGItem) item, a, a+1);
				a++;
			}
			
			return inventory.getItems().replace((ARPGItem) item, a, a+1);
		}
		return false;
	}
	
	public boolean removeItem(InventoryItem item, int amount) {
		int i;
		if(inventory.getItems().get(item) > 0) {
			int a = inventory.getItems().get(item);
			for (i = 0; i < amount - 1; i++) {
				inventory.getItems().replace((ARPGItem) item, a, a-1);
				a--;
			}
			return inventory.getItems().replace((ARPGItem) item, a, a-1);
		}
			return false;
	}
	
	public void switchItem() {
		int a = keySet.indexOf(usedItem);
		if (a + 1 == keySet.size()) a = -1;
		usedItem = keySet.get(a+1);
		System.out.println(usedItem);
	}
    
	public void useItem() {
		if (usedItem == ARPGItem.Bomb && inventory.getItems().get(ARPGItem.Bomb) > 0 && !isDisplacementOccurs()) {
			boolean b = getOwnerArea().registerActor(new Bombs(this.getOwnerArea(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()), 100));
			if (b) removeItem(ARPGItem.Bomb, 1);
		}
		if (usedItem == ARPGItem.Bow) {
			
		}
		if (usedItem == ARPGItem.Arrow) {
			
		}
		if (usedItem == ARPGItem.Sword) {
			
		}
		if (usedItem == ARPGItem.Staff) {
			
		}
		if (usedItem == ARPGItem.CastleKey) {
			
		}
		
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
