package ch.epfl.cs107.play.game.rpg.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Inventory {
	
	private int maxWeight;
	private Map<InventoryItem, Integer> items = new HashMap<InventoryItem, Integer> ();
	
	protected Inventory(int maxWeight) {
		this.maxWeight = maxWeight;
	}
	
	public int getOverallWeight() {
		return 0;
	}

	public int getMaxWeight() {
		return maxWeight;
	}
	
	public int getOverallPrice() {
		return 0;
	}
	
	protected boolean addItem(InventoryItem item, int amount) {
		int k = 0;
		if ((getOverallWeight() + (amount*item.getWeight())) <= getMaxWeight()) {
			for(Entry<InventoryItem, Integer> entry : items.entrySet()) {
				if(item == entry) {
					items.put(entry.getKey(), entry.getValue() + amount);
					++k;
				}
			}
			if (k == 0) items.put(item, amount);
		}
		return((getOverallWeight() + (amount*item.getWeight())) <= getMaxWeight());
	}
	
	protected boolean removeItem(InventoryItem item, int amount) {
		boolean i = false;
		for(Map.Entry<InventoryItem, Integer> entry : items.entrySet()) {
			if((item == entry) && ((entry.getValue() - amount) > 0)) {
				items.put(entry.getKey(), entry.getValue() - amount);
				i = true;
			}else if ((item == entry) && ((entry.getValue() - amount) == 0)) {
				items.remove(entry);
				i = true;
			}else i = false;
		}return i;
	}
	
	public boolean isItemStocked(InventoryItem item) {
		int k = 0;
		for(Map.Entry<InventoryItem, Integer> entry : items.entrySet()) {
			if(item == entry) {
				++k;
			}
		} return(k != 0);
	}

	
}
