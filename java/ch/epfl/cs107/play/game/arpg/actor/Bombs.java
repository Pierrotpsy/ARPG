package ch.epfl.cs107.play.game.arpg.actor;

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
import ch.epfl.cs107.play.game.arpg.actor.collectables.ARPGCollectableAreaEntity;
import ch.epfl.cs107.play.game.arpg.actor.mobs.ARPGMobs;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class Bombs extends AreaEntity implements Interactor {

	private final static int ANIMATION_DURATION = 8;
	private int timer;
	private RPGSprite sprite = new RPGSprite("zelda/bomb", 1, 1, this, new RegionOfInterest(0, 0, 16, 16));
	private int isExploding = 0;
	private boolean safety = false;
	private boolean isCellSpaceTaken = true;
	private ARPGBombHandler handler = new ARPGBombHandler();
	
	
	Sprite[][] sprites = RPGSprite.extractSprites("zelda/explosion", 7, 1.5f, 1.5f, this, 32, 32);

	Animation animation = RPGSprite.createSingleAnimation(ANIMATION_DURATION/2, sprites);
	
	public Bombs(Area area, DiscreteCoordinates position, int timer) {
		super(area, Orientation.DOWN, position);
		this.timer = timer; 
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public boolean takeCellSpace() {
		return isCellSpaceTaken;
	}

	@Override
	public boolean isCellInteractable() {
		return false;
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
	public void draw(Canvas canvas) {
		if (isExploding > 0) {
			animation.draw(canvas);
		} else if (isCellSpaceTaken){
			sprite.draw(canvas);
		}
		
	}
	
	@Override
	public void update(float deltaTime) {
		if (timer > 0) {
			timer -=0.1 ;
		} 
		
		if (isExploding > 0) {
			safety = false;
			isExploding--;
		}
		if (timer == 0) {
			isExploding = 8;
			safety = true;
			isCellSpaceTaken = false;
			timer = -1;
		}
		
		if (isExploding > 0) {
			animation.update(deltaTime);
		} else animation.reset();
		
		if (isExploding == 0 && !isCellSpaceTaken) {
			getOwnerArea().unregisterActor(this);
		}
		super.update(deltaTime);
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return getCurrentMainCellCoordinates().getNeighbours();
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
	
	public void setExplode() {
		timer = 0;
	}
	
	private class ARPGBombHandler implements ARPGInteractionVisitor {
		
		@Override
		public void interactWith(Grass grass) {
			if (isExploding > 0 && safety) {
				grass.slice();
			}
		}
		
		@Override
		public void interactWith(ARPGPlayer player) {
			if (isExploding > 0 && safety) {
				player.damage(20);			
			}
		}

		@Override
		public void interactWith(CollectableAreaEntity object) {
			if (isExploding > 0 && safety) {
				((ARPGCollectableAreaEntity) object).collect();		
			}
		}

		@Override
		public void interactWith(Bombs bomb) {
			if (isExploding > 0 && safety) {
				bomb.setExplode();	
			}	
		}
		
		@Override
		public void interactWith(ARPGMobs mob) {
			if (isExploding > 0 && mob.isVulnerableFire() && safety) {
				mob.damage(100);
			}
		}
	}
}
