package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class Bombs extends AreaEntity implements Interactor {

	private final static int ANIMATION_DURATION = 8;
	private int timer;
	private int i;
	private RPGSprite sprite;
	private boolean isExploding;
	
	
	Sprite[][] sprites = RPGSprite.extractSprites("zelda/explosion", 4, 1, 2, this, 16, 32, new Orientation[] {Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT});

	Animation [] animations = RPGSprite.createAnimations(ANIMATION_DURATION/2, sprites);
	
	public Bombs(Area area, Orientation orientation, DiscreteCoordinates position, int timer) {
		super(area, orientation, position);
		this.timer = timer;
		sprite = new RPGSprite("zelda/bomb", 1, 2, this, new RegionOfInterest(0, 0, 16, 32));
	}
	
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		// TODO Auto-generated method stub
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public boolean takeCellSpace() {
		return (timer != 0);
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
			animations[i].draw(canvas);

		} else {
			sprite.draw(canvas);
		}
		
	}
	
	@Override
	public void update(float deltaTime) {
		if (timer > 0) {
			timer -= 1;
		} if (timer == 0) {
			animations[i].update(deltaTime);
			getOwnerArea().unregisterActor(this);
		}
		super.update(deltaTime);
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		// TODO Auto-generated method stub
		return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
	}

	@Override
	public boolean wantsCellInteraction() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean wantsViewInteraction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void interactWith(Interactable other) {
		// TODO Auto-generated method stub
		
	}
	
}
