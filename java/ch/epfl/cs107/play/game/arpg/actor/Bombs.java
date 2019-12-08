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
import ch.epfl.cs107.play.game.arpg.ARPGCollectableAreaEntity;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class Bombs extends AreaEntity implements Interactor {

	private final static int ANIMATION_DURATION = 8;
	private int timer;
	private RPGSprite sprite = new RPGSprite("zelda/bomb", 1, 1, this, new RegionOfInterest(0, 0, 16, 16));
	private boolean isExploding = false;
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
		return true;
	}

	@Override
	public boolean isCellInteractable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isViewInteractable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
	}

	@Override
	public void draw(Canvas canvas) {
		if (isExploding) {
			animation.draw(canvas);

		} else {
			sprite.draw(canvas);
		}
		
	}
	
	@Override
	public void update(float deltaTime) {
		timer -=0.1 ;
		if (timer == 0) {
			isExploding = true;
			animation.update(deltaTime);
			getOwnerArea().unregisterActor(this);
			
			
		} else animation.reset();
		
		super.update(deltaTime);
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return getCurrentMainCellCoordinates().getNeighbours();
	}

	@Override
	public boolean wantsCellInteraction() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean wantsViewInteraction() {
		if(isExploding) {
			return true;

		}
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);
		
	}
	
	private class ARPGBombHandler implements ARPGInteractionVisitor {
		
		@Override
		public void interactWith(Grass grass) {
			grass.slice();
		}
		
		@Override
		public void interactWith(ARPGPlayer player) {
			player.damage(20);
		}

		@Override
		public void interactWith(CollectableAreaEntity object) {
			((ARPGCollectableAreaEntity) object).collect();
		}

		
	}
}
