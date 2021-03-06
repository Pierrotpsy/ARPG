package ch.epfl.cs107.play.game.arpg.actor.collectables;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.ARPGItem;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.inventory.InventoryItem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class CastleKey extends ARPGCollectableAreaEntity implements InventoryItem{

	private ARPGItem CastleKey = ARPGItem.CastleKey;
	private static final int VALUE = 1;
	private static final String NAME = "CastleKey";
	private RPGSprite sprite = new RPGSprite("zelda/key", 1, 1, this, new RegionOfInterest(0, 0, 16, 16));
	
	
	public CastleKey(Area area, DiscreteCoordinates position) {
		super(area, NAME, position, VALUE);
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
	@Override
	public float getWeight() {
		return CastleKey.getWeight();
	}

	@Override
	public int getPrice() {
		return CastleKey.getPrice();
	}

	@Override
	public String getPath() {
		return CastleKey.getPath();
	}
	
	public ARPGItem getKey() {
		return CastleKey;
	}
	
}