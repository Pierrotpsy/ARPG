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
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Bow extends ARPGCollectableAreaEntity implements InventoryItem {
	private ARPGItem Bow = ARPGItem.Bow;
	private static final String NAME = "Bow";

	private RPGSprite sprite = new RPGSprite("zelda/bow.icon", 1, 1, this, new RegionOfInterest(0, 0, 16, 16));
	
	public Bow(Area area, DiscreteCoordinates position) {
		super(area, NAME, position, 1);
	}
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
	}

	@Override
	public void draw(Canvas canvas) {
		sprite.draw(canvas);
	}

	//Getters
	public ARPGItem getBow() {
		return Bow;
	}
	
	@Override
	public float getWeight() {
		return Bow.getWeight();
	}

	@Override
	public int getPrice() {
		return Bow.getPrice();
	}

	@Override
	public String getPath() {
		return Bow.getPath();
	}
}
