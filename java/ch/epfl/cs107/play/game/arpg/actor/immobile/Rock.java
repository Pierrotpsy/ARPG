package ch.epfl.cs107.play.game.arpg.actor.immobile;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Heart;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class Rock extends AreaEntity {
	private static final double PROBABILITY_TO_DROP_ITEM = 0.7;
	private boolean IsCellSpaceTaken = true;
	
	private RPGSprite sprite = new RPGSprite("rock.1", 1, 1, this, new RegionOfInterest(0, 0, 16, 16));
	
	public Rock(Area area, DiscreteCoordinates position) {
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
		sprite.draw(canvas);
	}

	@Override
	public void update(float deltaTime) {
		
		if(!IsCellSpaceTaken) {
			dropItem();
			getOwnerArea().unregisterActor(this);
		}
		super.update(deltaTime);
	}

	public void setTakeCellSpace() {
		IsCellSpaceTaken = false;
	}
	
	private void dropItem() {
		double rate = RandomGenerator.getInstance().nextDouble();
		if (rate < PROBABILITY_TO_DROP_ITEM) {
				getOwnerArea().registerActor(new Heart(getOwnerArea(), getCurrentMainCellCoordinates(), 20));
		}	
	}
	
	public void destroy() {
		setTakeCellSpace();
	}

}


