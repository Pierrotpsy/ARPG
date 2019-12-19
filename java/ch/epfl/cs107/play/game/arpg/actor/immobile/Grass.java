package ch.epfl.cs107.play.game.arpg.actor.immobile;

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
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Grass extends AreaEntity {
	
	//Constants for drops 
	private static final double PROBABILITY_TO_DROP_ITEM = 0.5;
	private static final double PROBABILITY_TO_DROP_HEART = 0.5;
	
	//Constant for animation duration
	private static final int ANIMATION_DURATION = 8;
	
	private boolean IsCellSpaceTaken = true;
	
	//Animation countdown
	private int slice = 0;
	
	//Sprite and animation
	private RPGSprite sprite = new RPGSprite("zelda/grass", 1, 1, this, new RegionOfInterest(0, 0, 16, 16));
	
	Sprite[][] cutGrassSprites = RPGSprite.extractSprites("zelda/grass.sliced", 4, 2, 2, this, 32, 32, new Vector(-0.5f, 0f), "horizontal");

	Animation cutGrassAnimation = RPGSprite.createSingleAnimation(ANIMATION_DURATION/2, cutGrassSprites);
	
	//Constructor for grass, orientation doesn't matter here
	public Grass(Area area, DiscreteCoordinates position) {
		super(area, Orientation.UP, position);
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
	public void draw(Canvas canvas) {
		if (!IsCellSpaceTaken) {
			cutGrassAnimation.draw(canvas);
		} else if (IsCellSpaceTaken) {
			sprite.draw(canvas);	
		}
	}

	@Override
	public void update(float deltaTime) {
		if (slice > 0) {
			slice--;
		}
		
		if (!IsCellSpaceTaken) {
			cutGrassAnimation.update(deltaTime);
		} else cutGrassAnimation.reset();
		
		if(!IsCellSpaceTaken && slice == 0) {
			dropItem();
			getOwnerArea().unregisterActor(this);
		}
		super.update(deltaTime);
	}

	public void setTakeCellSpace() {
		IsCellSpaceTaken = false;
	}
	
	//Calculates if an item is to be dropped
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
	
	//Method to begin the destruction of the grass
	public void slice() {
		slice = 8;
		setTakeCellSpace();
	}

}
