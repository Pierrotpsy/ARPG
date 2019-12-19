package ch.epfl.cs107.play.game.arpg.actor.projectiles;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.FlyableEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.immobile.Bombs;
import ch.epfl.cs107.play.game.arpg.actor.immobile.Grass;
import ch.epfl.cs107.play.game.arpg.actor.mobs.ARPGMobs;
import ch.epfl.cs107.play.game.arpg.actor.mobs.FireSpell;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class Arrow extends Projectile implements FlyableEntity{
	private int maxDistance;
	private int velocity;
	private ARPGArrowHandler handler = new ARPGArrowHandler();
	private RPGSprite upArrowSprite = new RPGSprite("zelda/arrow", 1, 1, this, new RegionOfInterest(0, 0, 32, 32));
	private RPGSprite rightArrowSprite = new RPGSprite("zelda/arrow", 1, 1, this, new RegionOfInterest(32, 0, 32, 32));
	private RPGSprite downArrowSprite = new RPGSprite("zelda/arrow", 1, 1, this, new RegionOfInterest(64, 0, 32, 32));
	private RPGSprite leftArrowSprite = new RPGSprite("zelda/arrow", 1, 1, this, new RegionOfInterest(96, 0, 32, 32));
	
	public Arrow(Area area, Orientation orientation, DiscreteCoordinates position, int maxDistance, int velocity) {
		super(area, orientation, position, maxDistance, velocity);
		this.maxDistance = maxDistance;
		this.velocity = velocity;
	}


	@Override
	public boolean takeCellSpace() {
		return false;
	}

	@Override
	public boolean isCellInteractable() {
		return false;
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
	public void update(float deltaTime) {
		if(maxDistance > 0) {
			maxDistance--;
			move(velocity);
		}
		
		if(!getOwnerArea().enterAreaCells(this, Collections.singletonList (getCurrentMainCellCoordinates().jump(getOrientation().toVector()))) || maxDistance == 0) {
			end();
		}
		
		super.update(deltaTime);
	}
	
	@Override
	public void draw(Canvas canvas) {
		switch(getOrientation()) {
			case UP:
				upArrowSprite.draw(canvas);
				break;
			case DOWN:
				downArrowSprite.draw(canvas);
				break;
			case RIGHT:
				rightArrowSprite.draw(canvas);
				break;
			case LEFT:
				leftArrowSprite.draw(canvas);
				break;
		}
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
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
	
	public void end() {
		getOwnerArea().unregisterActor(this);
	}

	@Override
	public void canFly() {		
	}
	
	private class ARPGArrowHandler extends ARPGProjectileHandler {
		@Override
		public void interactWith(ARPGMobs mob) {
			if (mob.isVulnerablePhysical()) {
				mob.damage(30);
			}
			end();
		}
		
		@Override
		public void interactWith(Grass grass) {
			grass.slice();
		}
		
		@Override
		public void interactWith(Bombs bomb) {
			bomb.setExplode();
			end();
		}
		
		@Override
		public void interactWith(FireSpell spell) {
			spell.extinguish();
		}
	}


}
