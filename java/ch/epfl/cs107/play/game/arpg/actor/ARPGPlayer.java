package ch.epfl.cs107.play.game.arpg.actor;


import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.inventory.Inventory.Holder;
import ch.epfl.cs107.play.game.rpg.inventory.InventoryItem;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.ARPG;
import ch.epfl.cs107.play.game.arpg.ARPGPlayerGUI;
import ch.epfl.cs107.play.game.arpg.actor.collectables.ARPGCollectableAreaEntity;
import ch.epfl.cs107.play.game.arpg.actor.collectables.CastleKey;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Coin;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Heart;
import ch.epfl.cs107.play.game.arpg.area.ARPGArea;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public class ARPGPlayer extends Player implements Holder {
	
	private float hp;
	private int i,j;
	private TextGraphics message;
	private ShapeGraphics HPbarGreen;
	private ShapeGraphics HPbarRed;
	private Sprite sprite;
	/// Animation duration in frame number
    private final static int ANIMATION_DURATION = 8;
    private final ARPGPlayerHandler handler = new ARPGPlayerHandler();
    
    private ARPGInventory inventory;
    private ARPGPlayerGUI GUI;
    private ARPGItem usedItem;
    private List<ARPGItem> keySet;
    private int isUsingSword = 0;
    private int isUsingStaff = 0;
    private int isUsingBow = 0;
    
//    Animations:
    Sprite[][] sprites = RPGSprite.extractSprites("zelda/player", 4, 1, 2, this, 16, 32, new Orientation[] {Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT});

    Animation[] animations = RPGSprite.createAnimations(ANIMATION_DURATION/2, sprites);
    
    Sprite[][] swordSprites = RPGSprite.extractSprites("zelda/player.sword", 4, 2, 2, this, 32, 32, new Orientation[] {Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
    
    Animation[] swordAnimations = RPGSprite.createAnimations(ANIMATION_DURATION/2, swordSprites);
    
    Sprite[][] staffSprites = RPGSprite.extractSprites("zelda/player.staff_water", 4, 2, 2, this, 32, 32, new Orientation[] {Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
    
    Animation[] staffAnimations = RPGSprite.createAnimations(ANIMATION_DURATION/2, staffSprites);
    
    Sprite[][] bowSprites = RPGSprite.extractSprites("zelda/player.bow", 4, 2, 2, this, 32, 32, new Orientation[] {Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
    
    Animation[] bowAnimations = RPGSprite.createAnimations(ANIMATION_DURATION/2, bowSprites);
	
    /**
	 * Demo actor
	 * 
	 */
	public ARPGPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
		super(owner, orientation, coordinates);
		this.hp = 100;
		message = new TextGraphics(Integer.toString((int)hp), 0.4f, Color.BLUE);
		HPbarGreen = new ShapeGraphics(new Polygon(new Vector(0.1f, 1.8f), new Vector((float) 1, 1.8f), new Vector((float) 1, 1.7f), new Vector( 0.1f, 1.7f)), Color.GREEN, Color.BLACK, 0.01f);
		HPbarGreen.setParent(this);
		message.setParent(this);
		message.setAnchor(new Vector(0.2f, 1.7f));
		sprite = sprites[0][0];
		GUI = new ARPGPlayerGUI(this);
		
		inventory = new ARPGInventory(100);
		inventory.addItem(ARPGItem.Bomb, 3);
		inventory.addItem(ARPGItem.Sword, 1);
		
		keySet = new ArrayList<ARPGItem>(getInventory().getItems().keySet());
		
		for(Map.Entry<ARPGItem, Integer> entry: getInventory().getItems().entrySet()) {
			if (entry.getValue() > 0) {
				usedItem = entry.getKey();
				return;
			}
		}

		resetMotion();
	}
	 
	 @Override
	    public void update(float deltaTime) {
		 super.update(deltaTime);
		 
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
	        }
	        
	        if (isDisplacementOccurs()) {
		        animations[i].update(deltaTime);
	        } else animations[i].reset();
	       
	        switch(getOrientation()) {
	        	case DOWN:
	        		j = 2;
	        		break;
	        	case UP:
	        		j = 0;
	        		break;
	        	case RIGHT:
	        		j = 1;
	        		break;
	        	case LEFT:
	        		j = 3;
	        		break;
	        }
	        
	        if (isUsingSword > 0) {
	        	swordAnimations[j].update(deltaTime);
	        	isUsingSword--;
	        } else swordAnimations[j].reset();
	        
	        if (isUsingStaff > 0) {
	        	staffAnimations[j].update(deltaTime);
	        	isUsingStaff--;
	        } else staffAnimations[j].reset();
	        
	        if (isUsingBow > 0) {
	        	bowAnimations[j].update(deltaTime);
	        	isUsingBow--;
	        } else bowAnimations[j].reset();
	       
	        
	        Button tab = keyboard.get(Keyboard.TAB);
			if (tab.isPressed()) {
				switchItem();
			}
			
			Button space = keyboard.get(Keyboard.SPACE);
			if (space.isPressed()) {
				useItem();
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
    
	@Override
	public void draw(Canvas canvas) {
		GUI.draw(canvas);
		
		if(isUsingSword > 0) {
			swordAnimations[j].draw(canvas);
		} else 	if (isUsingBow > 0) {
			bowAnimations[j].draw(canvas);
		} else if (isUsingStaff > 0) {
			staffAnimations[j].draw(canvas);
		} else animations[i].draw(canvas);
		
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

	public boolean strengthen(int points) {
		if (hp == 100) {
			return false;
		} else if (hp + points > 100) {
			hp = 100;
			return true;
		} else {
			hp += points;
			return true;
		}
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
		Button space = keyboard.get(Keyboard.SPACE);
		
		if (e.isPressed() && usedItem == ARPGItem.Sword) {
			isUsingSword = 8;
			return true;
		} else if (space.isPressed() && usedItem == ARPGItem.CastleKey) {
			return true;
		}
		return false;
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);
	}
    
    public void damage(int d) {
    	hp -= d;
    }
    
    
//    Inventory:
	public boolean possess(ARPGItem item) {
		// TODO Auto-generated method stub
    	return inventory.isItemStocked(item);
	}
	
	@Override
	public boolean possess(InventoryItem item) {
		// TODO Auto-generated method stub
		return false;
	}
    
    protected void switchItem() {
		int a = keySet.indexOf(usedItem);
		if (a + 1 == keySet.size()) a = -1;
		while (inventory.getItems().get(keySet.get(a+1)) == 0) {
			a += 1;
			if (a + 1 == keySet.size()) a = -1;
		}
		usedItem = keySet.get(a+1);
	}
    
	protected void useItem() {
		if (usedItem == ARPGItem.Bomb && getInventory().isItemStocked(usedItem) && !isDisplacementOccurs()) {
			getOwnerArea().registerActor(new Bombs(this.getOwnerArea(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()), 100));
			getInventory().removeItem(ARPGItem.Bomb, 1);
		}
		if (usedItem == ARPGItem.Bow) {
			isUsingBow = 8;
		}
		if (usedItem == ARPGItem.Arrow) {
			
		}
		if (usedItem == ARPGItem.Sword) {
			
		}
		if (usedItem == ARPGItem.Staff) {
			isUsingStaff = 8;
		}
		if (usedItem == ARPGItem.CastleKey) {
			
		}
	}
	
	public String getUsedItemPath() {
		return usedItem.getPath();
	}
	
	public ARPGInventory getInventory() {
		return inventory;
	}

	
	private class ARPGPlayerHandler implements ARPGInteractionVisitor {
		@Override
		public void interactWith(Door door){
			
			
			if (door instanceof CastleDoor && usedItem == ARPGItem.CastleKey ) {
				if (!door.isOpen()) {
					 ((CastleDoor) door).openDoor();
				} else {
					setIsPassingADoor(door);
					((CastleDoor) door).closeDoor();
				}
				
			} else {
				setIsPassingADoor(door);
			}
	    }
		
		@Override
		public void interactWith(Grass grass) {
			grass.slice();
		}
		
		@Override
		public void interactWith(Bombs bomb) {
			bomb.setExplode();
		}
		
		public void interactWith(Coin coin) {
			inventory.addMoney(coin.getValue());
			coin.collect();
		}
		
		@Override
		public void interactWith(Heart heart) {
			if (strengthen(heart.getValue())) heart.collect();
		}

		@Override
		public void interactWith(CastleKey key) {
			key.collect();
			inventory.addItem(key.getKey(), 1);
		}
	}
}
