package ch.epfl.cs107.play.game.arpg.actor;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ch.epfl.cs107.play.game.rpg.inventory.Inventory;
import ch.epfl.cs107.play.game.rpg.inventory.InventoryItem;

public class ARPGInventory extends Inventory {
	private Map<ARPGItem, Integer> items = new HashMap<ARPGItem, Integer> ();
	private int money;
	
	public ARPGInventory(int maxWeight, int initialMoney) {
		super(maxWeight);
		money = initialMoney;
		items.put(ARPGItem.Arrow, 0);
		items.put(ARPGItem.Bow, 0);
		items.put(ARPGItem.Sword, 0);
		items.put(ARPGItem.Bomb, 0);
		items.put(ARPGItem.Staff, 0);
		items.put(ARPGItem.CastleKey, 0);
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
	
	public boolean addMoney(int coins) {
		if (money == 999) {
			return false;
		} else if (money + coins > 999) {
			money = 999;
			return true;
		} else {
			money += coins;
			return true;
		}
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

	
	public boolean addItem(ARPGItem item, int amount) {
		int i;
		if (getOverallWeight() + amount*item.getWeight() < getMaxWeight()) {
			int a = items.get(item);
			for (i = 0; i < amount; i++) {
				items.replace((ARPGItem) item, a, a+1);
				a++;
			}
			
			return true;
		}
		return false;
	}
	
	public boolean removeItem(ARPGItem item, int amount) {
		int i;
		if(items.get(item) > 0) {
			int a = items.get(item);
			for (i = 0; i < amount; i++) {
				items.replace((ARPGItem) item, a, a-1);
				a--;
			}
			return true;
		}
			return false;

	}


	
	public boolean isItemStocked(ARPGItem item) {
		
		return items.get(item) > 0;
	}
}
