package ch.epfl.cs107.play.game.arpg.actor;


import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.ARPGPlayerGUI;
import ch.epfl.cs107.play.game.arpg.actor.collectables.CastleKey;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Coin;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Heart;
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

public class ARPGPlayer extends Player {
	
	private float hp;
	private int i,j;
	private ShapeGraphics HPbarGreen;
	private ShapeGraphics HPbarRed;
	
	/// Animation duration in frame number
    private final static int ANIMATION_DURATION = 8;
    private final static int ARROW_MAX_DISTANCE = 6;
    private final static int ARROW_VELOCITY = 2;
    private final static int MAGIC_MAX_DISTANCE = 6;
    private final static int MAGIC_VELOCITY = 2;
    private final ARPGPlayerHandler handler = new ARPGPlayerHandler();
    
    private ARPGInventory inventory;
    private ARPGPlayerGUI GUI;
    private ARPGItem usedItem;
    private List<ARPGItem> keySet;
    private int isUsingSword = 0;
    private int isUsingStaff = 0;
    private int isUsingBow = 0;
    private enum states {
		IDLE,
		SWORDATTACK,
		BOWATTACK,
		STAFFATTACK,
		USINGCASTLEKEY;
		
	}
	private states state;
//    Animations:
    Sprite[][] sprites = RPGSprite.extractSprites("zelda/player", 4, 1, 2, this, 16, 32, new Orientation[] {Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT});

    Animation[] animations = RPGSprite.createAnimations(ANIMATION_DURATION/2, sprites);
    
    Sprite[][] swordSprites = RPGSprite.extractSprites("zelda/player.sword", 4, 2, 2, this, 32, 32, new Vector(-0.5f, 0f), new Orientation[] {Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
    
    Animation[] swordAnimations = RPGSprite.createAnimations(ANIMATION_DURATION/2, swordSprites);
    
    Sprite[][] staffSprites = RPGSprite.extractSprites("zelda/player.staff_water", 4, 2, 2, this, 32, 32, new Vector(-0.5f, 0f), new Orientation[] {Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
    
    Animation[] staffAnimations = RPGSprite.createAnimations(ANIMATION_DURATION/2, staffSprites);
    
    Sprite[][] bowSprites = RPGSprite.extractSprites("zelda/player.bow", 4, 2, 2, this, 32, 32, new Vector(-0.5f, 0f), new Orientation[] {Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
    
    Animation[] bowAnimations = RPGSprite.createAnimations(ANIMATION_DURATION/2, bowSprites);
	
    /**
	 * Demo actor
	 * 
	 */
	public ARPGPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
		super(owner, orientation, coordinates);
		this.hp = 100;		
		inventory = new ARPGInventory(100, 0);
		inventory.addItem(ARPGItem.CastleKey, 1);
		inventory.addItem(ARPGItem.Sword, 1);
		inventory.addItem(ARPGItem.Bow, 1);
		inventory.addItem(ARPGItem.Arrow, 10);
		inventory.addItem(ARPGItem.Staff, 1);
		inventory.addItem(ARPGItem.Bomb, 10);
		
		GUI = new ARPGPlayerGUI(this);
		
		keySet = new ArrayList<ARPGItem>(getInventory().getItems().keySet());
		
		for(Map.Entry<ARPGItem, Integer> entry: getInventory().getItems().entrySet()) {
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
		//updateState();
		updateHP();
		updateAnimations(deltaTime);
		
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
			
	}
	
    private void updateState() {
    	switch (state) {
	    	case IDLE:
	    		break;
	    	case SWORDATTACK:
	    		break;
	    	case STAFFATTACK:
	    		break;
	    	case BOWATTACK:
	    		break;
	    	case USINGCASTLEKEY:
	    		break;
    	}
    }
    
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
    
    private void updateAnimations(float deltaTime) {

		
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
	}


	private void moveOrientate(Orientation orientation, Button b){
	    
		if(b.isDown()) {
			if(getOrientation() == orientation) move(ANIMATION_DURATION);
	        	else orientate(orientation);
	        }
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
	
	public ARPGPlayer getPlayer() {
		return this;
	}
	
	@Override
	public boolean wantsViewInteraction() {
		Keyboard keyboard= getOwnerArea().getKeyboard();
		Button e = keyboard.get(Keyboard.E);
		if (e.isPressed() || state == states.SWORDATTACK) {
			return true;
		} else if (state == states.USINGCASTLEKEY) {
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
    	return inventory.isItemStocked(item);
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
	
	public String getUsedItemPath() {
		return usedItem.getPath();
	}
	
	public ARPGInventory getInventory() {
		return inventory;
	}

	
	private class ARPGPlayerHandler implements ARPGInteractionVisitor {
		@Override
		public void interactWith(Door door){
			if (door instanceof CastleDoor) {
				interactWith((CastleDoor)door);
			} else {
				setIsPassingADoor(door);
			}

	    }
		
		@Override
		public void interactWith(CastleDoor door) {
			if (!door.isOpen()) {
				state = states.IDLE;
				door.openDoor();
			} else {
				setIsPassingADoor(door);
				door.closeDoor();
			}
		}
		@Override
		public void interactWith(ARPGMobs mob) {
			if(isUsingSword > 7 && state == states.SWORDATTACK && mob.isVulnerablePhysical()) {
				mob.damage(20);
			}
		}
		
		@Override
		public void interactWith(Grass grass) {
			if (state == states.SWORDATTACK) grass.slice();
		}
		
		@Override
		public void interactWith(Bombs bomb) {
			if (state == states.SWORDATTACK) bomb.setExplode();
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
