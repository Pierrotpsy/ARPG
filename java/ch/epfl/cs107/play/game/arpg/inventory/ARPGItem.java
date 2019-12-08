package ch.epfl.cs107.play.game.arpg.inventory;

import ch.epfl.cs107.play.game.rpg.inventory.InventoryItem;

public enum ARPGItem implements InventoryItem{
	Arrow("Arrow", "zelda/arrow.icon", 1f, 10),
	Sword("Sword", "zelda/sword.icon", 5f, 50),
	Staff("Staff", "zelda/staff_water.icon", 3f, 100),
	Bow("Bow", "zelda/bow.icon", 3f, 30),
	Bomb("Bomb", "zelda/bomb", 0.2f, 10),
	CastleKey("Castle Key", "zelda/key", 0f, 500),;
	
	private String name;
	private String path;
	private float weight;
	private int price;
	
	
	ARPGItem(String name, String path, float weight, int price) {
		this.name = name;
		this.path = path;
		this.weight = weight;
		this.price = price;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public float getWeight() {
		// TODO Auto-generated method stub
		return this.weight;
	}

	@Override
	public int getPrice() {
		
		return this.price;
	}
}
