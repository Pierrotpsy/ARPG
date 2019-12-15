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

public class DarkLord extends ARPGMobs{
	private static final int ANIMATION_DURATION = 8;
	private final static double PROBABILITY_TO_MOVE = 0.1;
	private final static double PROBABILITY_TO_CHANGE_DIRECTION = 0.3;
	private final static int MAX_HP = 100;
	private float hp;
	private int isDying = 0;
	private ShapeGraphics HPbarGreen;
	private ShapeGraphics HPbarRed;
	private ARPGLordHandler handler = new ARPGLordHandler();
	private int inactionCooldown = 0;
	private int i;
	private Orientation orientation;
	private boolean isCellSpaceTaken = true;
	private enum states {
		IDLE,
		ATTACKING,
		SUMMONING,
		SUMMONTELEPORTATION,
		TELEPORT;
		
	}
	private states state;
	
	Sprite[][] lordSprites = RPGSprite.extractSprites("zelda/darkLord", 3, 2, 2, this, 32, 32, new Vector(-0.5f, 0f), new Orientation[] {Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});

    Animation[] lordAnimation = RPGSprite.createAnimations(ANIMATION_DURATION/2, lordSprites);
    
    Sprite[][] attackingLordSprites = RPGSprite.extractSprites("zelda/darkLord.spell", 4, 2, 2, this, 32, 32, new Vector(-0.5f, 0f), new Orientation[] {Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});

    Animation[] attackingLordAnimation = RPGSprite.createAnimations(ANIMATION_DURATION/2, attackingLordSprites, true);
    
    Sprite[][] vanishSprites = RPGSprite.extractSprites("zelda/vanish", 6, 2, 2, this, 32, 32, new Vector(-0.5f, 0f));

	Animation vanishAnimation = RPGSprite.createSingleAnimation(ANIMATION_DURATION/2, vanishSprites);
	
	public DarkLord(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
		super(area, orientation, coordinates);
		hp = MAX_HP;
		this.orientation = orientation;
		System.out.println(orientation);
		state = states.IDLE;
		inactionCooldown = 24;
	}
	@Override
	public void update(float deltaTime) {
		if (hp == 0 && isDying == 0) {
			kill();
		} else if (inactionCooldown > 0) {
			inactionCooldown--;
		}
		if (isDying > 0) {
			isDying--;
		}
		if (inactionCooldown == 0) {
			updateState();
		}
		
		if (hp > 0) {
			 HPbarGreen = null;
			 HPbarRed = null;
			 HPbarGreen = new ShapeGraphics(new Polygon(new Vector(0f, 1.8f), new Vector((float) hp/100f, 1.8f), new Vector((float) hp/100f, 1.7f), new Vector(0f, 1.7f)), Color.GREEN, Color.BLACK, 0.01f);
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
		
		 if (isDisplacementOccurs() || state == states.TELEPORT) {
		        lordAnimation[i].update(deltaTime);
	        } else lordAnimation[i].reset();
	     
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
	     if (state == states.ATTACKING || state == states.SUMMONING || state == states.SUMMONTELEPORTATION) {
	    	 attackingLordAnimation[i].update(deltaTime);
	     } else attackingLordAnimation[i].reset();
	     
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
					//move();
				}
				inactionCooldown = 20;
				break;
			case ATTACKING:
				/*while (move(ANIMATION_DURATION)) {
					move(ANIMATION_DURATION);
				}*/
				move(ANIMATION_DURATION);
				state = states.TOSLEEP;
				break;
			case SUMMONTELEPORTATION:
				inactionCooldown = RandomGenerator.getInstance().nextInt(12) + 30;
				state = states.ASLEEP;
				break;
			case TELEPORT:
				if (inactionCooldown == 0) {
					state = states.AWAKENING;
				}
			case SUMMONING:
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
		isDying = 8;
		isCellSpaceTaken = false;
	}
	
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		switch(state) {
		case IDLE:
		case ASLEEP:
		case AWAKENING:
			return getCurrentMainCellCoordinates().getNumberInFront(orientation, 8);
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
			System.out.println(getCurrentCells());
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
	
	private class ARPGLordHandler extends ARPGMobHandler {
		@Override
		public void interactWith(Grass grass) {
		}
		
		@Override
		public void interactWith(ARPGPlayer player) {
			if (state == states.ATTACKING) {
				player.damage(20);
			} else {
				state = states.ATTACKING;
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
