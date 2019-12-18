package ch.epfl.cs107.play.game.arpg.actor.mobs;

import java.awt.Color;
import java.util.Collections;
import java.util.List;
import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.actor.Bombs;
import ch.epfl.cs107.play.game.arpg.actor.Grass;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Coin;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class LogMonster extends ARPGMobs{
	private static final int ANIMATION_DURATION = 8;
	private final static double PROBABILITY_TO_MOVE = 0.2;
	private final static double PROBABILITY_TO_CHANGE_DIRECTION = 0.6;
	private final static int MAX_SLEEPING_DURATION = 240;
	private final static int MIN_SLEEPING_DURATION = 120;
	private final static int MAX_HP = 50;
	private float hp;
	private int isDying = 0;
	private ShapeGraphics HPbarGreen;
	private ShapeGraphics HPbarRed;
	private ARPGLogHandler handler = new ARPGLogHandler();
	private int inactionCooldown = 0;
	private int cooldown = 20;
	private int i;
	private int moveCount;
	private Orientation orientation;
	private boolean isCellSpaceTaken = true;
	private enum states {
		IDLE,
		ATTACKING,
		ASLEEP,
		AWAKENING,
		TOSLEEP;
		
	}
	private states state;
	
	Sprite[][] logSprites = RPGSprite.extractSprites("zelda/logMonster", 4, 2, 2, this, 32, 32, new Vector(-0.5f, 0f), new Orientation[] {Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});

    Animation[] logAnimation = RPGSprite.createAnimations(ANIMATION_DURATION/2, logSprites);
    
    Sprite[][] sleepingLogSprites = RPGSprite.extractSprites("zelda/logMonster.sleeping", 4, 2, 2, this, 32, 32, new Vector(-0.5f, 0f), "vertical");

    Animation sleepingLogAnimation = RPGSprite.createSingleAnimation(ANIMATION_DURATION/2, sleepingLogSprites, true);
    
    Sprite[][] awakeningLogSprites = RPGSprite.extractSprites("zelda/logMonster.wakingUp", 3, 2, 2, this, 32, 32, new Vector(-0.5f, 0f), "vertical");

    Animation awakeningLogAnimation = RPGSprite.createSingleAnimation(ANIMATION_DURATION/2, awakeningLogSprites, true);
    
    Sprite[][] vanishSprites = RPGSprite.extractSprites("zelda/vanish", 6, 2, 2, this, 32, 32, new Vector(-0.5f, 0f), "horizontal");

	Animation vanishAnimation = RPGSprite.createSingleAnimation(ANIMATION_DURATION/2, vanishSprites);
	
	public LogMonster(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
		super(area, orientation, coordinates);
		hp = MAX_HP;
		this.orientation = orientation;
		System.out.println(orientation);
		state = states.IDLE;
		inactionCooldown = 24;
	}
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (hp == 0 && isDying == 0) {
			kill();
		} else if (inactionCooldown > 0) inactionCooldown--;
		
		if (cooldown > 0) cooldown--;
		
		if (isDying > 0) isDying--;
		
		if (inactionCooldown == 0) {
			System.out.println(state);
			updateState();
		}
		
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
		
		 if (isDisplacementOccurs()) {
		        logAnimation[i].update(deltaTime);
	        } else logAnimation[i].reset();
	     
		 if (isDying > 0) {
			 
		 }
		 
		 if (state == states.ASLEEP) {
			 sleepingLogAnimation.update(deltaTime);
		 } else sleepingLogAnimation.reset();
		
		 if (state == states.AWAKENING) {
			 awakeningLogAnimation.update(deltaTime);
		 } else awakeningLogAnimation.reset();
		 
	     switch(getOrientation()) {
	       	case DOWN:
	        	i = 0;
	        	break;
	        	
	        case UP:
	        	i = 3;
	        	break;
	        	
	        case RIGHT:
	        	i = 2;
	        	break;
	        	
	        case LEFT:
	        	i = 1;
	        	break;
	       }
	     
	     switch (state) {
	     	case ASLEEP:
	     		sleepingLogAnimation.update(deltaTime);
	     		break;
	     	case AWAKENING:
	     		awakeningLogAnimation.update(deltaTime);
	     		break;
	     	case TOSLEEP:
	     	case ATTACKING:
	     	case IDLE:
	     		break;
	     }
	     
	     if (isDying > 0) {
	    	 vanishAnimation.update(deltaTime);
	     } else vanishAnimation.reset();
	     
	     if (isDying == 0 && isCellSpaceTaken == false) {
	    	 getOwnerArea().registerActor(new Coin(getOwnerArea(), getCurrentMainCellCoordinates(), 50));
	    	 getOwnerArea().unregisterActor(this);
	     }
	}
	
	public void updateState() {
		switch (state) {
			case IDLE:
				if (!isDisplacementOccurs()) {
					move();
				}
				inactionCooldown = RandomGenerator.getInstance().nextInt(3) + 20;
				break;
			case ATTACKING:
				while (move(ANIMATION_DURATION) && moveCount > 0) {
					move(ANIMATION_DURATION);
					moveCount--;
				}
				if ((!move(ANIMATION_DURATION) && cooldown > 0) || moveCount == 0) {
					state = states.TOSLEEP;
					moveCount = 0;
				}
				break;
			case TOSLEEP:
				inactionCooldown = RandomGenerator.getInstance().nextInt(MAX_SLEEPING_DURATION - MIN_SLEEPING_DURATION) + MIN_SLEEPING_DURATION;
				state = states.ASLEEP;
				break;
			case ASLEEP:
				if (inactionCooldown == 0) {
					state = states.AWAKENING;
				}
			case AWAKENING:
				state = states.IDLE;
				break;
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		if (isDying > 0) {
			vanishAnimation.draw(canvas);
		} else if (isCellSpaceTaken) {
			switch (state) {
				case ATTACKING:
				case TOSLEEP:
				case IDLE:
					logAnimation[i].draw(canvas);
					break;
				case ASLEEP:
					sleepingLogAnimation.draw(canvas);
					break;
				case AWAKENING:
					awakeningLogAnimation.draw(canvas);
					break;
			}
		}
		
		if (HPbarGreen != null) {
			HPbarGreen.draw(canvas);

		}
		
		if (HPbarRed != null) {
			HPbarRed.draw(canvas);
		}
	}
	
	public void kill() {
		isDying = 16;
		isCellSpaceTaken = false;
	}
	
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		switch(state) {
		case AWAKENING:
		case ASLEEP:
		case TOSLEEP:
			return Collections.singletonList(getCurrentMainCellCoordinates());
		case IDLE:
			return getCurrentMainCellCoordinates().getNumberInFront(orientation, 5);
		case ATTACKING:
			return Collections.singletonList (getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
		default:
			return Collections.singletonList (getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
		}
		
	}

	@Override
	public boolean wantsCellInteraction() {
		return true;
	}

	@Override
	public boolean wantsViewInteraction() {
		return true;
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);	
	}
	
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
		return false;
	}
	
	@Override
	public void damage(int dmg) {
		hp -= dmg;
	}
	
	private class ARPGLogHandler extends ARPGMobHandler {
		@Override
		public void interactWith(Grass grass) {
		}
		
		@Override
		public void interactWith(ARPGPlayer player) {
			if (state == states.ATTACKING && cooldown == 0) {
				player.damage(20);
				cooldown = 24;
			} else {
				state = states.ATTACKING;
				moveCount = 6;
			}
		}
		
		public void interactWith(ARPGMobs mob) {
		}
		
		@Override
		public void interactWith(CollectableAreaEntity object) {
		}

		@Override
		public void interactWith(Bombs bomb) {			
		}
	}

}
