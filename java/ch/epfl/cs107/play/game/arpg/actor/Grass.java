package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;


import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class Grass extends AreaEntity {
	
	private boolean IsCellSpaceTaken = true;
	//Sprite[][] sprites = RPGSprite.extractSprites("zelda/grass.sliced", 4, 1, 1, this, 16, 16, new Orientation[] {Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT});
	//Animation[] animation = RPGSprite.createAnimations(1, sprites);
	private RPGSprite sprite = new RPGSprite("zelda/grass", 1, 1, this, new RegionOfInterest(0, 0, 16, 16));
	
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
		if (IsCellSpaceTaken) {
			sprite.draw(canvas);
		} else {
			/*animation[0].draw(canvas);
			animation[1].draw(canvas);
			animation[2].draw(canvas);
			animation[3].draw(canvas);*/
		}

	}

	public void setTakeCellSpace() {
		IsCellSpaceTaken = false;
	}
	
	public void slice() {
		setTakeCellSpace();
		getOwnerArea().unregisterActor(this);
		
	}
	
	
}
