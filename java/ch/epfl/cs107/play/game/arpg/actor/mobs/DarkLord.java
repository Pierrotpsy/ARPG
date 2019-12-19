package ch.epfl.cs107.play.game.arpg.actor.mobs;

import java.awt.Color;
import java.util.ArrayList;
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
import ch.epfl.cs107.play.game.arpg.actor.collectables.CastleKey;
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

public class DarkLord extends ARPGMobs{
	//Constants
	private static final int ANIMATION_DURATION = 8;
	private static final int TELEPORTATION_RADIUS = 4;
	private final static int MAX_SPELL_WAIT_DURATION = 240;
	private final static double ATTACK_RATE = 0.5;
	private final static int MIN_SPELL_WAIT_DURATION = 120;
	private final static double PROBABILITY_TO_MOVE = 0.1;
	private final static double PROBABILITY_TO_CHANGE_DIRECTION = 0.3;
	private final static int MAX_HP = 300;
	private final static int TELEPORTATION_COOLDOWN = 10;
	
	
	private float hp;
	private int isDying = 0;
	private ShapeGraphics HPbarGreen;
	private ShapeGraphics HPbarRed;
	private ARPGLordHandler handler = new ARPGLordHandler();
	private int inactionCooldown = 0;
	private int spellCooldown = 0;
	private int teleportationCooldown = 0;
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
    
    Sprite[][] vanishSprites = RPGSprite.extractSprites("zelda/vanish", 6, 2, 2, this, 32, 32, new Vector(-0.5f, 0f), "horizontal");

	Animation vanishAnimation = RPGSprite.createSingleAnimation(ANIMATION_DURATION/2, vanishSprites);
	
	public DarkLord(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
		super(area, orientation, coordinates);
		hp = MAX_HP;
		this.orientation = orientation;
		state = states.IDLE;
		inactionCooldown = 24;
	}
	@Override
	public void update(float deltaTime) {
		updateHPBar();
		updateAnimations(deltaTime);
		if (inactionCooldown == 0) {
			updateState();
		} 
		
		if (hp == 0 && isDying == 0) {
			kill();
		} else if (inactionCooldown > 0) {
			inactionCooldown--;
		}
		if (isDying > 0) {
			isDying--;
		}
		
		if (spellCooldown > 0) {
			spellCooldown--;
		}
		
		if (teleportationCooldown > 0) {
			teleportationCooldown--;
		}
		
	     if (isDying == 0 && isCellSpaceTaken == false) {
	    	 getOwnerArea().registerActor(new CastleKey(getOwnerArea(), getCurrentMainCellCoordinates()));
	    	 getOwnerArea().unregisterActor(this);
	     }
	     super.update(deltaTime);
	}
	
	//Changes the state of the dark lord
	public void updateState() {
		switch (state) {
			case IDLE:
				if (spellCooldown == 0) {
					attack();
				} else {
					move();
					inactionCooldown = RandomGenerator.getInstance().nextInt(10);
				}
				break;
			case ATTACKING:
				if(!isTeleportationOccurs()) {
					orientateToAttack();
					getOwnerArea().registerActor(new FireSpell(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()), 5));
					state = states.IDLE;
				}
				break;
			case SUMMONTELEPORTATION:
				teleportationCooldown = TELEPORTATION_COOLDOWN;
				state = states.TELEPORT;
				break;
			case TELEPORT:
				if (teleportationCooldown == 0 && !isTeleportationOccurs()) {
					teleport();
					state = states.IDLE;
				}
				break;
			case SUMMONING:
				if(!isTeleportationOccurs()) {
					orientateToAttack();
					getOwnerArea().registerActor(new FlameSkull(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector())));
					state = states.IDLE;
				}
				break;
		}
	}
	
	private void updateHPBar() {
		if (hp > 0) {
			 HPbarGreen = null;
			 HPbarRed = null;
			 HPbarGreen = new ShapeGraphics(new Polygon(new Vector(0f, 2f), new Vector((float) hp/MAX_HP, 2f), new Vector((float) hp/MAX_HP, 1.9f), new Vector(0f, 1.9f)), Color.GREEN, Color.BLACK, 0.01f);
			 HPbarRed = new ShapeGraphics(new Polygon(new Vector((float) hp/MAX_HP, 2f), new Vector(1f, 2f), new Vector(1f, 1.9f), new Vector((float) hp/MAX_HP, 1.9f)), Color.RED, Color.BLACK, 0.01f);
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
		        lordAnimation[i].update(deltaTime);
	        } else lordAnimation[i].reset();
	     
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
	     if (state == states.ATTACKING || state == states.SUMMONING || state == states.SUMMONTELEPORTATION) {
	    	 attackingLordAnimation[i].update(deltaTime);
	     } else attackingLordAnimation[i].reset();
	     
	     if (isDying > 0) {
	    	 vanishAnimation.update(deltaTime);
	     } else vanishAnimation.reset();
	    
	}
	
	//Determines if the dark lord will launch flames or summon mobs
	public void attack() {
		spellCooldown = RandomGenerator.getInstance().nextInt(MAX_SPELL_WAIT_DURATION - MIN_SPELL_WAIT_DURATION) + MIN_SPELL_WAIT_DURATION;
		double rate = RandomGenerator.getInstance().nextDouble();
		if (rate < ATTACK_RATE) {
			state = states.SUMMONING;
		} else {
			state = states.ATTACKING;
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
	
	//Teleports the dark lord to random coordinates in his radius
	public void teleport() {
		List<DiscreteCoordinates> area = getCurrentMainCellCoordinates().getNeighboursInRadius(TELEPORTATION_RADIUS);
		Collections.shuffle(area);
		boolean safety = false;
		DiscreteCoordinates target = null;
		int maxtries = 10;
		while (!safety && maxtries > 0) {
			maxtries--;
			for(DiscreteCoordinates t : area) {
				if (getOwnerArea().enterAreaCells(this, Collections.singletonList(t)) && getOwnerArea().leaveAreaCells(this, Collections.singletonList(getCurrentMainCellCoordinates()))) {
					safety = true;
					target = t;
				}
			}
		}
		if (safety) super.teleport(target);
	}
	
	@Override
	public void draw(Canvas canvas) {
		if (isDying > 0) {
			vanishAnimation.draw(canvas);
		} else if (isCellSpaceTaken) {
			switch (state) {
				case IDLE:
				case TELEPORT:
					lordAnimation[i].draw(canvas);
					break;
				case SUMMONING:
				case ATTACKING:
				case SUMMONTELEPORTATION:
					attackingLordAnimation[i].draw(canvas);
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
	
	//Kills the dark lord
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
		return getCurrentMainCellCoordinates().getNeighboursInRadius(3);
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
	
	//Vulnerabilites
	@Override
	public boolean isVulnerableFire() {
		return false;
	}

	@Override
	public boolean isVulnerablePhysical() {
		return false;
	}

	@Override
	public boolean isVulnerableMagic() {
		return true;
	}
	
	//damages by a given amount
	@Override
	public void damage(int dmg) {
		hp -= dmg;
	}
	
	//Handler for the dark lord
	private class ARPGLordHandler extends ARPGMobHandler {
		
		//Summons teleportation if the player is too close
		@Override
		public void interactWith(ARPGPlayer player) {
			if (state != states.SUMMONTELEPORTATION && state != states.TELEPORT) {
				inactionCooldown = 0;
				state = states.SUMMONTELEPORTATION;
			}
		}
	}

}
