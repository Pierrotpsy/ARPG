package ch.epfl.cs107.play.game.arpg.actor.mobs;

import java.awt.Color;
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
import ch.epfl.cs107.play.game.arpg.actor.collectables.Coin;
import ch.epfl.cs107.play.game.arpg.actor.immobile.Bombs;
import ch.epfl.cs107.play.game.arpg.actor.immobile.Grass;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class FlameSkull extends ARPGMobs implements FlyableEntity{
	//Constants
	private static final int ANIMATION_DURATION = 8;
	private final static double PROBABILITY_TO_MOVE = 0.2;
	private final static double PROBABILITY_TO_CHANGE_DIRECTION = 0.4;
	private final static int MAX_HP = 40;
	private final static int ACTION_COOLDOWN = 20;
	
	private float hp;
	private boolean safety = false;
	private int isDying = 0;
	private ShapeGraphics HPbarGreen;
	private ShapeGraphics HPbarRed;
	private ARPGSkullHandler handler = new ARPGSkullHandler();
	private int cooldown = ACTION_COOLDOWN;
	private int i;
	private Orientation orientation;
	
	//Animation
	Sprite[][] skullSprites = RPGSprite.extractSprites("zelda/flameSkull", 3, 2, 2, this, 32, 32, new Vector(-0.5f, 0f), new Orientation[] {Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});

    Animation[] skullAnimation = RPGSprite.createAnimations(ANIMATION_DURATION/2, skullSprites);
    
    Sprite[][] vanishSprites = RPGSprite.extractSprites("zelda/vanish", 6, 2, 2, this, 32, 32, new Vector(-0.5f, 0f), "horizontal");

	Animation vanishAnimation = RPGSprite.createSingleAnimation(ANIMATION_DURATION/2, vanishSprites);
	
	//Constructor for FlameSkull
	public FlameSkull(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
		super(area, orientation, coordinates);
		hp = MAX_HP;
		this.orientation = orientation;
	}
	
	@Override
	public void update(float deltaTime) {
		updateHPBar();
		updateAnimations(deltaTime);
		
		if (!isDisplacementOccurs()) {
			move();
		}
		
		if (hp > 0) {
			hp -= 0.2;
		}
		
		if (hp == 0 && !safety) {
			kill();
		} else if (cooldown > 0) {
			cooldown--;
		}
		
		if (isDying > 0) {
			isDying--;
		}
	     
	     if (isDying == 0 && safety) {
	    	 getOwnerArea().unregisterActor(this);
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
		        skullAnimation[i].update(deltaTime);
	        } else skullAnimation[i].reset();
	       
	     switch(getOrientation()) {
	       	case DOWN:
	        	i = 2;
	        	break;
	        	
	        case UP:
	        	i = 0;
	        	break;
	        	
	        case RIGHT:
	        	i = 1;
	        	break;
	        	
	        case LEFT:
	        	i = 3;
	        	break;
	       }
	        
	     if (isDying > 0) {
	    	 vanishAnimation.update(deltaTime);
	     } else vanishAnimation.reset();
	}
	
	@Override
	public void draw(Canvas canvas) {
		if (isDying > 0) {
			vanishAnimation.draw(canvas);
		} else if (!safety) {
			skullAnimation[i].draw(canvas);
		}
		
		if (HPbarGreen != null) {
			HPbarGreen.draw(canvas);

		}
		
		if (HPbarRed != null) {
			HPbarRed.draw(canvas);
		}
	}
	
	//Kills the skull
	public void kill() {
		isDying = 16;
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
		return true;
	}

	@Override
	public boolean wantsViewInteraction() {
		return false;
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);	
	}
	
	//Random movements
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
		return false;
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
		return false;
	}

	@Override
	public boolean isVulnerablePhysical() {
		return true;
	}

	@Override
	public boolean isVulnerableMagic() {
		return true;
	}
	
	@Override
	public void damage(int dmg) {
		hp -= dmg;
	}

	//FlameSkull can fly
	@Override
	public void canFly() {
	}
	
	//Handler for flameskull
	private class ARPGSkullHandler extends ARPGMobHandler {
		//Slices grass
		@Override
		public void interactWith(Grass grass) {
			grass.slice();
		}
		
		//Damages player
		@Override
		public void interactWith(ARPGPlayer player) {
			if (cooldown == 0) {
				player.damage(20);
				cooldown = 20;
			}
			
		}
		//Damages mobs vulnerable to fire
		@Override
		public void interactWith(ARPGMobs mob) {
			if(mob.isVulnerableFire() && cooldown == 0) {
				mob.damage(20);
				cooldown = 10;
			}
		}

		//Makes bombs explode
		@Override
		public void interactWith(Bombs bomb) {
			bomb.setExplode();
			
		}
	}

}
