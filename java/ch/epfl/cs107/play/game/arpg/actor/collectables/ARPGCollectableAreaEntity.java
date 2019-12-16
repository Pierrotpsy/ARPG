package ch.epfl.cs107.play.game.arpg.actor.collectables;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class ARPGCollectableAreaEntity extends CollectableAreaEntity {
	
	private static final int ANIMATION_DURATION = 8;
	private String name;
	private DiscreteCoordinates position;
	private int value;
	
	
	public ARPGCollectableAreaEntity (Area area, String name, DiscreteCoordinates position, int value) {
		super(area, Orientation.DOWN, position);
		this.name = name;
		this.position = position;
		this.value = value;
	}
	
	public void collect() {
		getOwnerArea().unregisterActor(this);
	}
	
	public String getName() {
		return name;
	}
	
	public int getValue() {
		return value;
	}
	
	@Override
	public void draw(Canvas canvas) {
	}
	
	@Override
	public void update(float deltaTime) {
	}
}

