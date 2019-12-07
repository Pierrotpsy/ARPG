package ch.epfl.cs107.play.game.arpg.inventory;

import ch.epfl.cs107.play.game.rpg.inventory.InventoryItem;

public enum ARPGItem implements InventoryItem{
	Arrow("Arrow", 1f, 10),
	Sword("Sword", 5f, 50),
	Staff("Staff", 3f, 100),
	Bow("Bow", 3f, 30),
	Bomb("Bomb", 0.2f, 10),
	CastleKey("Castle Key", 0f, 500),;
	
	
	
	
	ARPGItem(String name, float weight, int price) {
		
	}
}
