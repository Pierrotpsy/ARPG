package ch.epfl.cs107.play.game.arpg.actor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.inventory.Inventory;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.TextAlign;
import ch.epfl.cs107.play.window.Canvas;

public class ARPGInventory extends Inventory {
	private Map<ARPGItem, Integer> items = new HashMap<ARPGItem, Integer> ();
	private int money;
	private RPGSprite inventoryBackgroundSprite;
	private ArrayList<ARPGItem> possessedItems = new ArrayList<ARPGItem>();
	private Sprite[][] inventorySlotSprite = new RPGSprite[4][2];
	private ArrayList<RPGSprite> gearSprite = new ArrayList<RPGSprite>();
	private ARPGPlayer player;
	private String weight = "Total weight " + Integer.toString(getOverallWeight());
	private TextGraphics weightGraphics;
	private TextGraphics title;
	
	public ARPGInventory(int maxWeight, int initialMoney, ARPGPlayer player) {
		super(maxWeight);
		money = initialMoney;
		this.player = player;
		items.put(ARPGItem.Arrow, 0);
		items.put(ARPGItem.Bow, 0);
		items.put(ARPGItem.Sword, 0);
		items.put(ARPGItem.Bomb, 0);
		items.put(ARPGItem.Staff, 0);
		items.put(ARPGItem.CastleKey, 0);
		
		inventoryBackgroundSprite = new RPGSprite("zelda/inventory.background", 9f, 9f, player, new RegionOfInterest(0, 0, 240, 240), new Vector(-4, -4), 1f, Float.MAX_VALUE);
	}
	
	//Returns the money and the total value of the items stored in the inventory
	@Override
	public int getOverallPrice() {
		int price = 0;
		for(Map.Entry<ARPGItem, Integer> entry: getItems().entrySet()) {
			price += entry.getValue()*entry.getKey().getPrice();
		}
		return price;
	}
	
	//Returns the overall weight of the items stored in the inventory
	@Override
	public int getOverallWeight() {
		int weight = 0;
		for(Map.Entry<ARPGItem, Integer> entry: getItems().entrySet()) {
			weight += entry.getValue()*entry.getKey().getWeight();
		}
		return weight;
	}
	
	//Adds money in the inventory, but the max amount is 999 coins
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
	
	//Getter for money
	public int getMoney() {
		return money;
	}
	
	//Getter for fortune
	public int getFortune() {
		return getOverallPrice() + getMoney();
	}

	//Getter for items
	public Map<ARPGItem, Integer> getItems() {
		return items;
	}

	//Method to add an certain amount of an item to the inventory
	protected boolean addItem(ARPGItem item, int amount) {
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
	
	//Method to remove a certain amount of an item from the inventory
	protected boolean removeItem(ARPGItem item, int amount) {
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
	
	//Returns true if there is at least one item of a specified category
	public boolean isItemStocked(ARPGItem item) {
		return items.get(item) > 0;
	}
	

    public void updateDrawnItems() {
    	ARPGItem[] keySet = {ARPGItem.Arrow, ARPGItem.Bow, ARPGItem.Sword, ARPGItem.Bomb, ARPGItem.Staff, ARPGItem.CastleKey}; 
    	possessedItems.clear();
    	for(int i = 0; i < keySet.length; ++i) {
    		if(player.possess(keySet[i])) {
    			possessedItems.add(keySet[i]);
    		}
    	}
    	updateItemIcons();
    }
   //////////
	public void updateItemIcons() {
		int k = 0;
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 2; ++j) {
				inventorySlotSprite[i][j] = new RPGSprite("zelda/inventory.slot", 1.5f, 1.5f, player, new RegionOfInterest(0, 0, 64, 64), new Vector((-3.25f + i*2), (1 - j*2)), 1f, Float.MAX_VALUE);
			}
		}
		if (possessedItems.size() > 4) {
			for (int i = 0; i < 4; i++) {
				gearSprite.add(new RPGSprite(possessedItems.get(k).getPath(), 0.85f, 0.85f, player, new RegionOfInterest(0, 0, 16, 16), new Vector(-3 + i*2, 1.25f), 1f, Float.MAX_VALUE));
				k++;
			}
			for (int i = 0; i < possessedItems.size() - 4; i++) {
				gearSprite.add(new RPGSprite(possessedItems.get(k).getPath(), 0.85f, 0.85f, player, new RegionOfInterest(0, 0, 16, 16), new Vector(-3 + i*2, 1.25f - 2), 1f, Float.MAX_VALUE));
				k++;
			}
		} else {
			for (int i = 0; i < possessedItems.size(); i++) {
				gearSprite.add(new RPGSprite(possessedItems.get(k).getPath(), 0.85f, 0.85f, player, new RegionOfInterest(0, 0, 16, 16), new Vector(-3 + i*2, 1.25f), 1f, Float.MAX_VALUE));
				k++;
			}
		}
		
		
		title = new TextGraphics("INVENTORY", 0.75f, Color.BLACK, null, 1f, false, false, new Vector(1, 4), TextAlign.Horizontal.CENTER, TextAlign.Vertical.TOP, 1f, Float.MAX_VALUE);
		title.setParent(this.player);
		weightGraphics = new TextGraphics(weight, 0.5f, Color.BLACK, null, 1f, false, false, new Vector(-1, -3), TextAlign.Horizontal.CENTER, TextAlign.Vertical.BOTTOM, 1f, Float.MAX_VALUE);
		weightGraphics.setParent(this.player);
	}
	
	public void draw(Canvas canvas) {
		inventoryBackgroundSprite.draw(canvas);
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 2; ++j) {
				inventorySlotSprite[i][j].draw(canvas);
			}
		}
		
		for (int i = 0; i < gearSprite.size(); ++i) {
			gearSprite.get(i).draw(canvas);
		}
	
		
		title.draw(canvas);
		weightGraphics.draw(canvas);
	}
}
