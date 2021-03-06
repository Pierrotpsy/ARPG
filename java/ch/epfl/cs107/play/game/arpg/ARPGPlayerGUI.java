package ch.epfl.cs107.play.game.arpg;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class ARPGPlayerGUI implements Graphics{
	private float width;
	private float height;
	private int i;
	private Vector anchor;
	private ARPGPlayer player;
	
	public ARPGPlayerGUI(ARPGPlayer player) {
		super();
		this.player = player;
	}
	
	@Override
	public void draw(Canvas canvas) {
	
		width = canvas.getScaledWidth();
		height = canvas.getScaledHeight();
		anchor = canvas.getTransform().getOrigin().sub(new Vector(width/2, height/2)); 
		
		ImageGraphics gearDisplay = new 
				ImageGraphics(ResourcePath.getSprite("zelda/gearDisplay"), 1.5f, 1.5f, new RegionOfInterest(0, 0, 32, 32), anchor.add(new Vector(0, height - 1.75f)), 1, Float.MAX_VALUE);
		gearDisplay.draw(canvas);		
		
		ImageGraphics gear = new 
				ImageGraphics(ResourcePath.getSprite(player.getUsedItemPath()), 0.75f, 0.75f, new RegionOfInterest(0, 0, 16, 16), anchor.add(new Vector(0.4f, height - 1.4f)), 1, Float.MAX_VALUE);
		gear.draw(canvas);
		
		ImageGraphics coinDisplay = new
		ImageGraphics(ResourcePath.getSprite("zelda/coinsDisplay"), 4f, 2f, new RegionOfInterest(0, 0, 64, 32), anchor.add(new Vector(0f, 0f)), 1, Float.MAX_VALUE);
		coinDisplay.draw(canvas);
		
		for (i = 0; i < Integer.toString(player.getInventory().getMoney()).length(); i++) {
			getMoneyDigits()[i].draw(canvas);
		}
	}
	
	//Method to get an ImageGraphics array of the digits of the money in the inventory
	public ImageGraphics[] getMoneyDigits() {
		String money = Integer.toString(player.getInventory().getMoney());
		ImageGraphics[] digits = new ImageGraphics[money.length()];
		
		for (int i = 0; i < money.length(); i++) {
			switch (money.charAt(i)) {
				case '1':
					digits[i] = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1f, 1f, new RegionOfInterest(0, 0, 16, 16), anchor.add(new Vector(1.3f + 0.75f*i, 0.55f)), 1, Float.MAX_VALUE);
					break;
				case '2':
					digits[i] = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1f, 1f, new RegionOfInterest(16, 0, 16, 16), anchor.add(new Vector(1.3f + 0.75f*i, 0.55f)), 1, Float.MAX_VALUE);
					break;
				case '3':
					digits[i] = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1f, 1f, new RegionOfInterest(32, 0, 16, 16), anchor.add(new Vector(1.3f + 0.75f*i, 0.55f)), 1, Float.MAX_VALUE);
					break;
				case '4':
					digits[i] = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1f, 1f, new RegionOfInterest(48, 0, 16, 16), anchor.add(new Vector(1.3f + 0.75f*i, 0.55f)), 1, Float.MAX_VALUE);
					break;
				case '5':
					digits[i] = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1f, 1f, new RegionOfInterest(0, 16, 16, 16), anchor.add(new Vector(1.3f + 0.75f*i, 0.55f)), 1, Float.MAX_VALUE);
					break;
				case '6':
					digits[i] = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1f, 1f, new RegionOfInterest(16, 16, 16, 16), anchor.add(new Vector(1.3f + 0.75f*i, 0.55f)), 1, Float.MAX_VALUE);
					break;
				case '7':
					digits[i] = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1f, 1f, new RegionOfInterest(32, 16, 16, 16), anchor.add(new Vector(1.3f + 0.75f*i, 0.55f)), 1, Float.MAX_VALUE);
					break;
				case '8':
					digits[i] = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1f, 1f, new RegionOfInterest(48, 16, 16, 16), anchor.add(new Vector(1.3f + 0.75f*i, 0.55f)), 1, Float.MAX_VALUE);
					break;
				case '9':
					digits[i] = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1f, 1f, new RegionOfInterest(0, 32, 16, 16), anchor.add(new Vector(1.3f + 0.75f*i, 0.55f)), 1, Float.MAX_VALUE);
					break;
				case '0':
					digits[i] = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1f, 1f, new RegionOfInterest(16, 32, 16, 16), anchor.add(new Vector(1.3f + 0.75f*i, 0.55f)), 1, Float.MAX_VALUE);
					break;
				
			}
		}
		return digits;
	}
}

