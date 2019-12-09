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

public class Coin extends ARPGCollectableAreaEntity {
	
	private static final int ANIMATION_DURATION = 8;
	private static final String NAME = "Coin";
	private Animation animation;
	
	
	public Coin(Area area, DiscreteCoordinates position, int value) {
		super(area, NAME, position, value);
		
		Sprite[] sprites = new RPGSprite[4];
		for (int i = 0; i < sprites.length; ++i) {
			sprites[i] = new RPGSprite("zelda/coin", 1, 1, this, new RegionOfInterest(16 * i, 0, 16, 16));
		}
		animation = new Animation(ANIMATION_DURATION, sprites, false);
	}
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
	}

	@Override
	public void draw(Canvas canvas) {
		animation.draw(canvas);
	}
	
	@Override
	public void update(float deltaTime) {
		animation.update(deltaTime);
	}
	
}