package ch.epfl.cs107.play.game.rpg.inventory;

import java.util.Map;

public class Inventory {
	private int maxWeight;
	private Map<InventoryItem, Integer> items;
	
	public Inventory(int maxWeight) {
		this.maxWeight = maxWeight;
	}
	
	public int getOverallPrice() {
		return 0;
	}
	
	public int getOverallWeight() {
		return 0;
	}

	public int getMaxWeight() {
		return maxWeight;
	}
}
