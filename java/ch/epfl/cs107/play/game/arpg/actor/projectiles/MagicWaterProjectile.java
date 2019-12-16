package ch.epfl.cs107.play.game.arpg.actor.projectiles;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.FlyableEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.mobs.ARPGMobs;
import ch.epfl.cs107.play.game.arpg.actor.mobs.FireSpell;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

public class MagicWaterProjectile extends Projectile implements FlyableEntity {
	private final static int ANIMATION_DURATION = 8;
	private int maxDistance;
	private int velocity;
	private boolean safety = false;
	private ARPGArrowHandler handler = new ARPGArrowHandler();
	
	Sprite[][] magicSprites = RPGSprite.extractSprites("zelda/magicWaterProjectile", 4, 1, 1, this, 32, 32, "horizontal");

	Animation magicAnimation = RPGSprite.createSingleAnimation(ANIMATION_DURATION/2, magicSprites, true);
	
	public MagicWaterProjectile(Area area, Orientation orientation, DiscreteCoordinates position, int maxDistance, int velocity) {
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
		magicAnimation.update(deltaTime);
		super.update(deltaTime);
	}
	
	@Override
	public void draw(Canvas canvas) {
		magicAnimation.draw(canvas);
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
		safety = true;
		getOwnerArea().unregisterActor(this);
	}

	@Override
	public void canFly() {		
	}
	
	private class ARPGArrowHandler extends ARPGProjectileHandler {
		@Override
		public void interactWith(ARPGMobs mob) {
			if (mob.isVulnerableMagic() && !safety) {
				mob.damage(30);
			}
			end();
		}
		
		@Override
		public void interactWith(FireSpell spell) {
			spell.extinguish();
		}
	}


}
