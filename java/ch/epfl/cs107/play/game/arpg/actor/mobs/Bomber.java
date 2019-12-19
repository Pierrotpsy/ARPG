package ch.epfl.cs107.play.game.arpg.actor.mobs;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.FlyableEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.immobile.Bombs;
import ch.epfl.cs107.play.game.arpg.actor.immobile.Grass;
import ch.epfl.cs107.play.game.arpg.actor.mobs.ARPGMobs.ARPGMobHandler;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Bomber extends ARPGMobs {
	//Constants for probability to move
	private final static double PROBABILITY_TO_MOVE = 0.5;
	private final static double PROBABILITY_TO_CHANGE_DIRECTION = 0.4;
	
	//Constant for animation duration
	private static final int ANIMATION_DURATION = 8;
	
	//Constants for cooldowns
	private final static int BOMB_COOLDOWN = 10;
	private final static int ACTION_COOLDOWN = 20;
	//Max hp
	private final static int MAX_HP = 40;
	
	private float hp;
	private boolean safety = false;
	
	//Cooldown for animation
	private int isDying = 0;
	
	private boolean isCellSpaceTaken;
	private ShapeGraphics HPbarGreen;
	private ShapeGraphics HPbarRed;
	private int cooldown = ACTION_COOLDOWN;
	private int i;
	private Orientation orientation;
	//Cooldown before the placement of a new bomb
	private int bombCooldown = BOMB_COOLDOWN;
	
	//Animation
	Sprite[][] sprites = RPGSprite.extractSprites("zelda/character", 4, 1, 2, this, 16, 32, new Orientation[] {Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});

    Animation[] animation = RPGSprite.createAnimations(ANIMATION_DURATION/2, sprites);
    
    Sprite[][] vanishSprites = RPGSprite.extractSprites("zelda/vanish", 6, 2, 2, this, 32, 32, new Vector(-0.5f, 0f), "horizontal");

	Animation vanishAnimation = RPGSprite.createSingleAnimation(ANIMATION_DURATION/2, vanishSprites);
	
	//Constructor for a bomber
	public Bomber(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
		super(area, orientation, coordinates);
		hp = MAX_HP;
		this.orientation = orientation;
	}
	@Override
	public void update(float deltaTime) {
		updateHPBar();
		updateAnimations(deltaTime);
		
		if (!isDisplacementOccurs() && hp > 0 && cooldown == 0) {
			move();
			cooldown = ACTION_COOLDOWN;
		}
		
		if (hp == 0 && !safety) {
			kill();
		} else if (cooldown > 0) {
			cooldown--;
		}
		if (bombCooldown > 0) bombCooldown--;
		
		if (isDying > 0) {
			isDying--;
		}
		

	     if (bombCooldown == 0) {
	    	 orientateToAttack();
	    	 getOwnerArea().registerActor(new Bombs(getOwnerArea(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()), 50));
	    	 bombCooldown = BOMB_COOLDOWN;
	     }
	     super.update(deltaTime);
	}
	
	private void updateHPBar() {
		if (hp > 0) {
			 HPbarGreen = null;
			 HPbarRed = null;
			 HPbarGreen = new ShapeGraphics(new Polygon(new Vector(0f, 1.8f), new Vector((float) hp/MAX_HP, 1.8f), new Vector((float) hp/MAX_HP, 1.7f), new Vector(0f, 1.7f)), Color.GREEN, Color.BLACK, 0.01f);
			 HPbarRed = new ShapeGraphics(new Polygon(new Vector((float) hp/MAX_HP, 1.8f), new Vector(1f, 1.8f), new Vector(1f, 1.7f), new Vector((float) hp/MAX_HP, 1.7f)), Color.RED, Color.BLACK, 0.01f);
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
		if (isDisplacementOccurs()) {
	        animation[i].update(deltaTime);
        } else animation[i].reset();
       
     switch(getOrientation()) {
       	case DOWN:
        	i = 2;
        	break;
        	
        case UP:
        	i = 0;
        	break;
        	
        case RIGHT:
        	i = 3;
        	break;
        	
        case LEFT:
        	i = 1;
        	break;
       }
        
     if (isDying > 0) {
    	 vanishAnimation.update(deltaTime);
     } else vanishAnimation.reset();
     
     if (isDying == 0 && safety) {
    	 getOwnerArea().registerActor(new Bomb(getOwnerArea(), getCurrentMainCellCoordinates()));
    	 getOwnerArea().unregisterActor(this);
     }
	}
	
	@Override
	public void draw(Canvas canvas) {
		if (isDying > 0) {
			vanishAnimation.draw(canvas);
		} else if (!safety) {
			animation[i].draw(canvas);
		}
		
		if (HPbarGreen != null) {
			HPbarGreen.draw(canvas);

		}
		
		if (HPbarRed != null) {
			HPbarRed.draw(canvas);
		}
	}
	
	//Checks all orientations and chooses a random one to attack
	public void orientateToAttack() {
		ArrayList<Orientation> list = new ArrayList<Orientation>();
		list.add(Orientation.DOWN);
		list.add(Orientation.UP);
		list.add(Orientation.RIGHT);
		list.add(Orientation.LEFT);
		Collections.shuffle(list);
		for (Orientation a : list) {
			if (getOwnerArea().registerActor(new FireSpell(getOwnerArea(), a, getCurrentMainCellCoordinates().jump(a.toVector()), 0))) {
				orientate(a);
				return;
			}
		}
	}
	
	//Kills the bomber
	public void kill() {
		isDying = 16;
		isCellSpaceTaken = false;
		safety = true;
	}
	
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return Collections.singletonList (getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
	}

	@Override
	public boolean wantsCellInteraction() {
		return false;
	}

	@Override
	public boolean wantsViewInteraction() {
		return true;
	}

	@Override
	public void interactWith(Interactable other) {
	}
	
	//Random movement 
	@Override
	public void move() {
		double chanceToMove = RandomGenerator.getInstance().nextDouble();
		double chanceToChangeDirection = RandomGenerator.getInstance().nextDouble();
		if (chanceToMove < PROBABILITY_TO_MOVE) {
			if (chanceToChangeDirection < PROBABILITY_TO_CHANGE_DIRECTION) {
				int randomInt = RandomGenerator.getInstance().nextInt(3);
				switch(randomInt) {
					case 0: 
						orientation = Orientation.UP;
						break;
					case 1:
						orientation = Orientation.DOWN;
						break;
					case 2:
						orientation = Orientation.LEFT;
						break;
					case 3:
						orientation = Orientation.RIGHT;
						break;
					
					
				}
			}
			
			if(getOrientation() == orientation) {
				move(ANIMATION_DURATION);
			} else {
            	orientate(orientation);
            }
		}
	}
	
	@Override
	public boolean takeCellSpace() {
		return isCellSpaceTaken;
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
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
	}
	
	//Vulnerabilities
	@Override
	public boolean isVulnerableFire() {
		return true;
	}

	@Override
	public boolean isVulnerablePhysical() {
		return true;
	}

	@Override
	public boolean isVulnerableMagic() {
		return true;
	}
	
	//Damage by a given amount
	@Override
	public void damage(int dmg) {
		hp -= dmg;
	}
	
}
