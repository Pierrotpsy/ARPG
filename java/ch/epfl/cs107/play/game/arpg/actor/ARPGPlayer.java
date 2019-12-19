package ch.epfl.cs107.play.game.arpg.actor;


import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.rpg.actor.Dialog;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.io.XMLTexts;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.ARPGPlayerGUI;
import ch.epfl.cs107.play.game.arpg.Test;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Bow;
import ch.epfl.cs107.play.game.arpg.actor.collectables.CastleKey;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Coin;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Heart;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Staff;
import ch.epfl.cs107.play.game.arpg.actor.immobile.CastleDoor;
import ch.epfl.cs107.play.game.arpg.actor.immobile.CaveDoor;
import ch.epfl.cs107.play.game.arpg.actor.mobs.ARPGMobs;
import ch.epfl.cs107.play.game.arpg.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.arpg.actor.projectiles.MagicWaterProjectile;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class ARPGPlayer extends Player {
	
	private float hp;
	private int i,j;
	private ShapeGraphics HPbarGreen;
	private ShapeGraphics HPbarRed;
	
    private final static int ARROW_MAX_DISTANCE = 6;
    private final static int ARROW_VELOCITY = 2;
    private final static int MAGIC_MAX_DISTANCE = 6;
    private final static int MAGIC_VELOCITY = 2;
    private final static int REGEN_COOLDOWN = 50;
    
	// Animation duration in frame number
    private final static int ANIMATION_DURATION = 8;
    
    //PlayerHandler
    private final ARPGPlayerHandler handler = new ARPGPlayerHandler();
    
    private ARPGInventory inventory;
    private ARPGPlayerGUI GUI;
    private ARPGItem usedItem;
    private List<ARPGItem> keySet;
    private int regenCooldown = 0;
    
    //Animation countdowns
    private int isUsingSword = 0;
    private int isUsingStaff = 0;
    private int isUsingBow = 0;
    
	private Dialog d;
	private boolean b = false;
	//States of the player
	private states state;
    private enum states {
		IDLE,
		SWORDATTACK,
		BOWATTACK,
		STAFFATTACK,
		USINGCASTLEKEY;
		
	}
    
    //Animations:
    Sprite[][] sprites = RPGSprite.extractSprites("zelda/player", 4, 1, 2, this, 16, 32, new Orientation[] {Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT});

    Animation[] animations = RPGSprite.createAnimations(ANIMATION_DURATION/2, sprites);
    
    Sprite[][] swordSprites = RPGSprite.extractSprites("zelda/player.sword", 4, 2, 2, this, 32, 32, new Vector(-0.5f, 0f), new Orientation[] {Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
    
    Animation[] swordAnimations = RPGSprite.createAnimations(ANIMATION_DURATION/2, swordSprites);
    
    Sprite[][] staffSprites = RPGSprite.extractSprites("zelda/player.staff_water", 4, 2, 2, this, 32, 32, new Vector(-0.5f, 0f), new Orientation[] {Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
    
    Animation[] staffAnimations = RPGSprite.createAnimations(ANIMATION_DURATION/2, staffSprites);
    
    Sprite[][] bowSprites = RPGSprite.extractSprites("zelda/player.bow", 4, 2, 2, this, 32, 32, new Vector(-0.5f, 0f), new Orientation[] {Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
    
    Animation[] bowAnimations = RPGSprite.createAnimations(ANIMATION_DURATION/2, bowSprites);
	
	
    //ARPGPlayer Constructor
	public ARPGPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
		super(owner, orientation, coordinates);
		this.hp = 100;		
		inventory = new ARPGInventory(100, 0, this);
		inventory.addItem(ARPGItem.Sword, 1);
		
		if (Test.MODE) {
			inventory.addItem(ARPGItem.Sword, 1);
			inventory.addItem(ARPGItem.Bow, 1);
			inventory.addItem(ARPGItem.Arrow, 10);
			inventory.addItem(ARPGItem.Bomb, 10);
			inventory.addItem(ARPGItem.Staff, 1);
			inventory.addItem(ARPGItem.CastleKey, 1);
		}
		
		GUI = new ARPGPlayerGUI(this);
		
		keySet = new ArrayList<ARPGItem>(inventory.getItems().keySet());
		
		for(Map.Entry<ARPGItem, Integer> entry: inventory.getItems().entrySet()) {
			if (entry.getValue() > 0) {
				usedItem = entry.getKey();
				return;
			}
		}
		
		state = states.IDLE;
		
		resetMotion();
	}
	 
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		updateHP();
		updateAnimations(deltaTime);
		inventory.updateDrawnItems();
		
		Keyboard keyboard= getOwnerArea().getKeyboard();
		if (state == states.IDLE) {
			moveOrientate(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
			moveOrientate(Orientation.UP, keyboard.get(Keyboard.UP));
			moveOrientate(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
			moveOrientate(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
		}
		
		Button tab = keyboard.get(Keyboard.TAB);
		if (tab.isPressed()) {
			switchItem();
		}
			
		Button space = keyboard.get(Keyboard.SPACE);
		if (space.isPressed()) {
			useItem();
		}
		
		Button q = keyboard.get(Keyboard.Q);
		if (q.isPressed()) {
			swap();
		}
		
		d = new Dialog(XMLTexts.getText("rock_interaction"), "dialog", getOwnerArea());
		
		if (state == states.IDLE && regenCooldown > 0) {
			regenCooldown--;
		} else if (state != states.IDLE) regenCooldown = REGEN_COOLDOWN;
		if (regenCooldown == 0) strengthen(2);
	
	}
    
	//Updates the HP bar
    private void updateHP() {
    	if (hp > 0) {
			 HPbarGreen = null;
			 HPbarRed = null;
			 HPbarGreen = new ShapeGraphics(new Polygon(new Vector(0f, 1.8f), new Vector((float) hp/100f, 1.8f), new Vector((float) hp/100f, 1.7f), new Vector( 0f, 1.7f)), Color.GREEN, Color.BLACK, 0.01f);
			 HPbarRed = new ShapeGraphics(new Polygon(new Vector((float) hp/100, 1.8f), new Vector(1f, 1.8f), new Vector(1f, 1.7f), new Vector((float) hp/100, 1.7f)), Color.RED, Color.BLACK, 0.01f);
			 HPbarGreen.setParent(this);
			 HPbarRed.setParent(this);
		 }
		 
		 if (hp < 0) {
			 hp = 0.f;
			 HPbarGreen = null;
			 HPbarRed = null;
			 HPbarRed = new ShapeGraphics(new Polygon(new Vector(0f, 1.8f), new Vector(1f, 1.8f), new Vector(1f, 1.7f), new Vector(0f, 1.7f)), Color.RED, Color.BLACK, 0.01f);
			 HPbarRed.setParent(this);
		 }
    }
    
    //Updates the animation according to which animation needs to be displayed
    private void updateAnimations(float deltaTime) {	
    	
    	inventory.getAnimations().get(indexOf(usedItem)).update(deltaTime);
    	

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
        } else {
        	state = states.IDLE;
        	swordAnimations[j].reset();
        }
        
        if (isUsingStaff > 0) {
        	staffAnimations[j].update(deltaTime);
        	isUsingStaff--;
        } else {
        	state = states.IDLE;
        	staffAnimations[j].reset();
        }
        
        if (isUsingBow > 0) {
        	bowAnimations[j].update(deltaTime);
        	isUsingBow--;
        } else {
        	state = states.IDLE;
        	bowAnimations[j].reset();
        }
        
    }
    
	@Override
	public void draw(Canvas canvas) {
		GUI.draw(canvas);
		//if (d != null) d.draw(canvas);

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
		if (b) {
			inventory.draw(canvas);
		}
	}

	//If the movement is in the player's orientation, moves, if not, orientates the player to the given orientation
	private void moveOrientate(Orientation orientation, Button b){
	    
		if(b.isDown()) {
			if(getOrientation() == orientation) move(ANIMATION_DURATION);
	        	else orientate(orientation);
	        }
	}

	//Adds a given amount of hp to the player, max hp is 100
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
	
	//Returns true if "E" is pressed, if the player performs a sword attack, or if he is using a castle key
	@Override
	public boolean wantsViewInteraction() {
		Keyboard keyboard= getOwnerArea().getKeyboard();
		Button e = keyboard.get(Keyboard.E);
		if (e.isPressed() || state == states.SWORDATTACK || state == states.USINGCASTLEKEY) {
			return true;
		}
		return false;
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);
	}
    
	//Damages the player by a given amount
    public void damage(int d) {
    	hp -= d;
    }
    
    
    //Returns true if the player possessed a given item
	public boolean possess(ARPGItem item) {
    	return inventory.isItemStocked(item);
	}
    
	private int indexOf(ARPGItem item) {
		for (i = 0; i < inventory.getPossessedItems().size(); i++) {
			if (item == inventory.getPossessedItems().get(i)) return i;
		}
		return 0;
	}
	
    private void swap() {
    	if (b) {
    		b = false;
    	} else if (!b) b = true;
    }
	
	//If invoked, moves the used Item to the next one on the list
    protected void switchItem() {
		int a = keySet.indexOf(usedItem);
		if (a + 1 == keySet.size()) a = -1;
		while (inventory.getItems().get(keySet.get(a+1)) == 0) {
			a += 1;
			if (a + 1 == keySet.size()) a = -1;
		}
		usedItem = keySet.get(a+1);
	}
    
    //For each usable item, makes it possible to use them
	protected void useItem() {
		if (usedItem == ARPGItem.Bomb && inventory.isItemStocked(usedItem)) {
			if (getOwnerArea().canEnterAreaCells(new Bombs(getOwnerArea(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()), 100), Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector())))) {
				getOwnerArea().registerActor(new Bombs(getOwnerArea(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()), 100));
				inventory.removeItem(ARPGItem.Bomb, 1);
			}
		}
		if (usedItem == ARPGItem.Bow && isUsingBow == 0) {
			if (inventory.isItemStocked(ARPGItem.Arrow) ) {
				if (getOwnerArea().canEnterAreaCells(new Arrow(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()), 5, 2), Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector())))) {
					getOwnerArea().registerActor(new Arrow(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()), ARROW_MAX_DISTANCE, ARROW_VELOCITY));
					inventory.removeItem(ARPGItem.Arrow, 1);
				}
			}
			state = states.BOWATTACK;
			isUsingBow = 8;
		}
		if (usedItem == ARPGItem.Sword && isUsingSword == 0) {
			state = states.SWORDATTACK;
			isUsingSword = 8;
		}
		if (usedItem == ARPGItem.Staff && isUsingStaff == 0) {
			getOwnerArea().registerActor(new MagicWaterProjectile(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()), MAGIC_MAX_DISTANCE, MAGIC_VELOCITY));
			state = states.STAFFATTACK;
			isUsingStaff = 8;
		}
		if (usedItem == ARPGItem.CastleKey) {
			state = states.USINGCASTLEKEY;
		}
	}
	
	//Returns the path of the used Item
	public String getUsedItemPath() {
		return usedItem.getPath();
	}
	
	//Returns the player's inventory
	public ARPGInventory getInventory() {
		return inventory;
	}
	
	//Is used to enable the player to interact with other entities
	private class ARPGPlayerHandler implements ARPGInteractionVisitor {
		//Passes through a Door
		@Override
		public void interactWith(Door door){
			if (door instanceof CastleDoor) {
				interactWith((CastleDoor)door);
			} else if (door instanceof CaveDoor) {
				interactWith((CaveDoor)door);
			} else{
				setIsPassingADoor(door);
			}

	    }
		
		//Opens a CastleDoor, and if open, passes through
		@Override
		public void interactWith(CastleDoor door) {
			if (!door.isOpen() && state == states.USINGCASTLEKEY) {
				state = states.IDLE;
				door.openDoor();
			} else  if (state == states.IDLE) {
				setIsPassingADoor(door);
				door.closeDoor();
			}
		}
		
		//Passes through an open CaveDoor
		@Override
		public void interactWith(CaveDoor door) {
			if (door.isOpen() && state == states.IDLE) {
				setIsPassingADoor(door);
			} 
		}
		
		//Damages mobs vulnerable to physical damage
		@Override
		public void interactWith(ARPGMobs mob) {
			if(isUsingSword > 7 && state == states.SWORDATTACK && mob.isVulnerablePhysical()) {
				mob.damage(20);
			}
		}
		
		//Slices grass if using the sword
		@Override
		public void interactWith(Grass grass) {
			if (state == states.SWORDATTACK) grass.slice();
		}
		
		//Makes a bomb explode if using the sword
		@Override
		public void interactWith(Bombs bomb) {
			if (state == states.SWORDATTACK) bomb.setExplode();
		}
		
		//Collects a coin
		public void interactWith(Coin coin) {
			inventory.addMoney(coin.getValue());
			coin.collect();
		}
		
		//Collects a heart
		@Override
		public void interactWith(Heart heart) {
			if (strengthen(heart.getValue())) heart.collect();
		}
		
		//Collects a castle key
		@Override
		public void interactWith(CastleKey key) {
			key.collect();
			inventory.addItem(key.getKey(), 1);
		}
		
		//Collects a staff
		@Override
		public void interactWith(Staff staff) {
			staff.collect();
			inventory.addItem(staff.getStaff(), 1);
		}
		
		//Collects a bow
		@Override
		public void interactWith(Bow bow) {
			bow.collect();
			inventory.addItem(bow.getBow(), 1);
			inventory.addItem(ARPGItem.Arrow, 10);
		}
		
		//Collects a bomb
		@Override
		public void interactWith(Bomb bomb) {
			bomb.collect();
			inventory.addItem(bomb.getBow(), 1);
		}
	}
	
}
