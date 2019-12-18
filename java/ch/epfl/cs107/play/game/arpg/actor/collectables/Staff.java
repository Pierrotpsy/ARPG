package ch.epfl.cs107.play.game.arpg.actor.collectables;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.ARPGItem;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.inventory.InventoryItem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Staff extends ARPGCollectableAreaEntity implements InventoryItem {
	
	private ARPGItem Staff = ARPGItem.Staff;
	private static final int ANIMATION_DURATION = 8;
	private static final String NAME = "Staff";

	Sprite[][] staffSprites = RPGSprite.extractSprites("zelda/staff", 8, 2, 2, this, 32, 32,new Vector (-0.5f, 0f), "horizontal");

    Animation staffAnimation = RPGSprite.createSingleAnimation(ANIMATION_DURATION/2, staffSprites, true);
	
	public Staff(Area area, DiscreteCoordinates position) {
		super(area, NAME, position, 1);
	}
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
	}

	@Override
	public void draw(Canvas canvas) {
		staffAnimation.draw(canvas);
	}
	
	@Override
	public void update(float deltaTime) {
		staffAnimation.update(deltaTime);
	}

	//Getters
	public ARPGItem getStaff() {
		return Staff;
	}
	
	@Override
	public float getWeight() {
		return Staff.getWeight();
	}

	@Override
	public int getPrice() {
		return Staff.getPrice();
	}

	@Override
	public String getPath() {
		return Staff.getPath();
	}
}
