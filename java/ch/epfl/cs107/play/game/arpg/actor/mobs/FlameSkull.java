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
import ch.epfl.cs107.play.game.arpg.actor.Bombs;
import ch.epfl.cs107.play.game.arpg.actor.Grass;
import ch.epfl.cs107.play.game.arpg.actor.collectables.ARPGCollectableAreaEntity;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;

public class FlameSkull extends ARPGMobs implements FlyableEntity{
	private static final int ANIMATION_DURATION = 8;
	private final static double PROBABILITY_TO_MOVE = 0.1;
	private final static double PROBABILITY_TO_CHANGE_DIRECTION = 0.3;
	private final static int MAX_HP = 100;
	private float hp;
	private boolean isDying = false;
	private ShapeGraphics HPbarGreen;
	private ShapeGraphics HPbarRed;
	private ARPGSkullHandler handler = new ARPGSkullHandler();
	private int cooldown = 20;
	private int i;
	private Orientation orientation;
	
	Sprite[][] skullSprites = RPGSprite.extractSprites("zelda/flameSkull", 3, 2, 2, this, 32, 32, new Vector(-0.5f, 0f), new Orientation[] {Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});

    Animation[] skullAnimation = RPGSprite.createAnimations(ANIMATION_DURATION/2, skullSprites);
    
    Sprite[][] vanishSprites = RPGSprite.extractSprites("zelda/vanish", 6, 2, 2, this, 32, 32);

	Animation vanishAnimation = RPGSprite.createSingleAnimation(ANIMATION_DURATION/2, vanishSprites);
	
	public FlameSkull(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
		super(area, orientation, coordinates);
		hp = MAX_HP;
		this.orientation = orientation;
	}
	@Override
	public void update(float deltaTime) {
		if (!isDisplacementOccurs()) {
			move();
		}
		
		if (hp == 0) {
			kill();
		} else if (cooldown > 0) {
			cooldown--;
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
	        
	     if (isDying) {
	    	 vanishAnimation.update(deltaTime);
	     } else vanishAnimation.reset();
	}
	
	@Override
	public void draw(Canvas canvas) {
		if (isDying) {
			vanishAnimation.draw(canvas);
		} else {
			skullAnimation[i].draw(canvas);
		}
		
		if (HPbarGreen != null) {
			HPbarGreen.draw(canvas);

		}
		
		if (HPbarRed != null) {
			HPbarRed.draw(canvas);
		}
	}
	
	public void kill() {
		isDying = true;
		getOwnerArea().unregisterActor(this);
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
		if(cooldown == 0) {
			cooldown = 10;
			return true;
		}
		return false;
	}

	@Override
	public boolean wantsViewInteraction() {
		return false;
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

	@Override
	public void canFly() {
	}
	
	private class ARPGSkullHandler extends ARPGMobHandler {
		@Override
		public void interactWith(Grass grass) {
			grass.slice();
		}
		
		@Override
		public void interactWith(ARPGPlayer player) {
			player.damage(20);
		}
		
		public void interactWith(ARPGMobs mob) {
			if(mob.isVulnerableFire()) {
				mob.damage(20);
			}
		}
		
		@Override
		public void interactWith(CollectableAreaEntity object) {
		}

		@Override
		public void interactWith(Bombs bomb) {
			bomb.setExplode();
			
		}
	}

}
