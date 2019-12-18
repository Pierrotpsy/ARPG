package ch.epfl.cs107.play.game.arpg.actor.mobs;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
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
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.window.Canvas;

public class FireSpell extends AreaEntity implements Interactor {
	//Constants
	private final static int ANIMATION_DURATION = 8;
	private final static int MIN_LIFETIME = 100;
	private final static int MAX_LIFETIME = 200;
	private final static int PROPAGATION_TIME_FIRE = 6;
	
	private int force;
	private int propagationCooldown = PROPAGATION_TIME_FIRE;
	private int lifetime;
	private int safety = -1;
	private Orientation orientation;
	private ARPGFlameHandler handler = new ARPGFlameHandler();
	
	//Animation
	Sprite[][] flameSprites = RPGSprite.extractSprites("zelda/fire", 7, 1, 1, this, 16, 16, "horizontal");

    Animation flameAnimation = RPGSprite.createSingleAnimation(ANIMATION_DURATION/2, flameSprites, true);
	
    //FireSpell Constructor
	public FireSpell(Area area, Orientation orientation, DiscreteCoordinates position, int force) {
		super(area, orientation, position);
		this.orientation = orientation;
		this.force = force;
		lifetime = RandomGenerator.getInstance().nextInt(MAX_LIFETIME - MIN_LIFETIME) + MIN_LIFETIME;	
		if (force == 0) {
			getOwnerArea().unregisterActor(this);
		}
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
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
		return false;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
	}

	@Override
	public void draw(Canvas canvas) {
		flameAnimation.draw(canvas);
		
	}
	
	@Override
	public void update(float deltaTime) {
		if (lifetime > 0) {
			lifetime--;
		} else if (lifetime == 0) {
			getOwnerArea().unregisterActor(this);
		}
		
		if (propagationCooldown > 0) {
			propagationCooldown--;
		} else if (propagationCooldown == 0 && force > 1 && safety < 0) {
			safety++;
			getOwnerArea().registerActor(new FireSpell(getOwnerArea(), orientation, getCurrentMainCellCoordinates().jump(getOrientation().toVector()), force -1));
		}
		flameAnimation.update(deltaTime);
		
	}
	
	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return null;
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
	
	//Destroys a firespell
	public void extinguish() {
		lifetime = 0;
	}
	
	//Handler for FireSpell
	private class ARPGFlameHandler implements ARPGInteractionVisitor {
			
			//Slices grass
			@Override
			public void interactWith(Grass grass) {
				grass.slice();
			}
			
			//Damages player
			@Override
			public void interactWith(ARPGPlayer player) {
				player.damage(force);			
			}
	
			//Destroys a collectable area entity
			@Override
			public void interactWith(CollectableAreaEntity object) {
				((ARPGCollectableAreaEntity) object).collect();		
			}
	
			//Makes a bomb explode
			@Override
			public void interactWith(Bombs bomb) {
				bomb.setExplode();	
			}
			
			//Damages mobs vulnerable to fire
			@Override
			public void interactWith(ARPGMobs mob) {
				if (mob.isVulnerableFire()) {
					mob.damage(force);
				}
			}
		}
}
