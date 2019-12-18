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

public class Bomb extends ARPGCollectableAreaEntity implements InventoryItem {
	private ARPGItem Bomb = ARPGItem.Bomb;
	private static final String NAME = "Bomb";

	private RPGSprite sprite = new RPGSprite("zelda/bomb", 1, 1, this, new RegionOfInterest(16, 0, 16, 16));
	
	public Bomb(Area area, DiscreteCoordinates position) {
		super(area, NAME, position, 1);
		System.out.println("ok");
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
		return Bomb;
	}
	
	@Override
	public float getWeight() {
		return Bomb.getWeight();
	}

	@Override
	public int getPrice() {
		return Bomb.getPrice();
	}

	@Override
	public String getPath() {
		return Bomb.getPath();
	}
}
