package ch.epfl.cs107.play.game.arpg.inventory;

import java.util.HashMap;
import java.util.Map;
import ch.epfl.cs107.play.game.rpg.inventory.Inventory;
import ch.epfl.cs107.play.game.rpg.inventory.InventoryItem;

public class ARPGInventory extends Inventory {
	private Map<ARPGItem, Integer> items = new HashMap<ARPGItem, Integer> ();
	private int money;
	
	public ARPGInventory(int maxWeight, int initialMoney) {
		super(maxWeight);
		money = initialMoney;
		getItems().put(ARPGItem.Arrow, 0);
		getItems().put(ARPGItem.Bow, 0);
		getItems().put(ARPGItem.Sword, 0);
		getItems().put(ARPGItem.Bomb, 0);
		getItems().put(ARPGItem.Staff, 0);
		getItems().put(ARPGItem.CastleKey, 0);
	}
	
	@Override
	public int getOverallPrice() {
		int price = 0;
		for(Map.Entry<ARPGItem, Integer> entry: getItems().entrySet()) {
			price += entry.getValue()*entry.getKey().getPrice();
		}
		return price;
	}
	
	@Override
	public int getOverallWeight() {
		int weight = 0;
		for(Map.Entry<ARPGItem, Integer> entry: getItems().entrySet()) {
			weight += entry.getValue()*entry.getKey().getWeight();
		}
		return weight;
	}
	
	public void addMoney(int coins) {
		money += coins;
	}
	
	public int getMoney() {
		return money;
	}
	
	public int getFortune() {
		return getOverallPrice() + getMoney();
	}

	public Map<ARPGItem, Integer> getItems() {
		return items;
	}

}
