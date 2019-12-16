package ch.epfl.cs107.play.game.arpg.actor.collectables;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class Heart extends ARPGCollectableAreaEntity {
	
	private static final int ANIMATION_DURATION = 8;
	private static final String NAME = "Heart";

	Sprite[][] heartSprites = RPGSprite.extractSprites("zelda/heart", 4, 1, 1, this, 16, 16, "horizontal");

    Animation heartAnimation = RPGSprite.createSingleAnimation(ANIMATION_DURATION/2, heartSprites, true);
	
	public Heart(Area area, DiscreteCoordinates position, int value) {
		super(area, NAME, position, value);
	}
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
	}

	@Override
	public void draw(Canvas canvas) {
		heartAnimation.draw(canvas);
	}
	
	@Override
	public void update(float deltaTime) {
		heartAnimation.update(deltaTime);
	}
	
}