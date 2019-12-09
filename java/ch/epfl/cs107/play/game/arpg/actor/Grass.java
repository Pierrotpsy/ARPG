package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;


import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.collectables.ARPGCollectableAreaEntity;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Coin;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Heart;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class Grass extends AreaEntity {
	private static final double PROBABILITY_TO_DROP_ITEM = 0.5;
	private static final double PROBABILITY_TO_DROP_HEART = 0.5;
	private static final int ANIMATION_DURATION = 8;
	private boolean IsCellSpaceTaken = true;
	
	private Animation animation;
	private RPGSprite sprite = new RPGSprite("zelda/grass", 1, 1, this, new RegionOfInterest(0, 0, 16, 16));
	
	
	public Grass(Area area, DiscreteCoordinates position) {
		super(area, Orientation.UP, position);
		
		Sprite[] sprites = new RPGSprite[4];
		for (int i = 0; i < sprites.length; ++i) {
			sprites[i] = new RPGSprite("zelda/grass.sliced", 1, 1, this, new RegionOfInterest(32 * i, 0, 32, 32));
		}
		animation = new Animation(ANIMATION_DURATION, sprites, false);
	}
	
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public boolean takeCellSpace() {
		return IsCellSpaceTaken;
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
		if (!IsCellSpaceTaken) {
			animation.draw(canvas);
		} else if (IsCellSpaceTaken) {
			sprite.draw(canvas);	
		}
	}

	@Override
	public void update(float deltaTime) {
		if (!IsCellSpaceTaken) {
			animation.update(deltaTime);
			getOwnerArea().unregisterActor(this);
		} else animation.reset();
		
		super.update(deltaTime);
	}

	public void setTakeCellSpace() {
		IsCellSpaceTaken = false;
	}
	
	private void dropItem() {
		double rate = RandomGenerator.getInstance().nextDouble();
		if (rate < PROBABILITY_TO_DROP_ITEM) {
			if (RandomGenerator.getInstance().nextDouble() < PROBABILITY_TO_DROP_HEART) {
				getOwnerArea().registerActor(new Heart(getOwnerArea(), getCurrentMainCellCoordinates(), 20));
			} else {
				getOwnerArea().registerActor(new Coin(getOwnerArea(), getCurrentMainCellCoordinates(), 50));
			}
		}	
	}
	
	public void slice() {
		setTakeCellSpace();
		dropItem();
		
	}

}
