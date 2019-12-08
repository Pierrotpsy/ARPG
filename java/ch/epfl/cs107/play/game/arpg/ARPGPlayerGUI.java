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
		
		ImageGraphics gearDisplay = new ImageGraphics(ResourcePath.getSprite("zelda/gearDisplay"), 1.5f, 1.5f, new RegionOfInterest(0, 0, 32, 32), anchor.add(new Vector(0, height - 1.75f)), 1, 0f);
		gearDisplay.draw(canvas);		
		
		ImageGraphics gear = new
		ImageGraphics(ResourcePath.getSprite(player.getUsedItemPath()), 0.75f, 0.75f, new RegionOfInterest(0, 0, 16, 16), anchor.add(new Vector(0.4f, height - 1.4f)), 1, 0f);
		gear.draw(canvas);
		
		ImageGraphics coinDisplay = new
		ImageGraphics(ResourcePath.getSprite("zelda/coinsDisplay"), 4f, 2f, new RegionOfInterest(0, 0, 64, 32), anchor.add(new Vector(0f, 0f)), 1, 0f);
		coinDisplay.draw(canvas);
		
		getMoneyDigits()[0].draw(canvas);
		getMoneyDigits()[1].draw(canvas);
		getMoneyDigits()[2].draw(canvas);
		
		/*
		if (getMoneyDigits().length != 0) {
			for (int i = 0; i < getMoneyDigits().length; i++) {
				try {
					getMoneyDigits()[i].draw(canvas);

				}
				catch(NullPointerException e) {
					System.out.println("Nullpointer");
				}
			}
		}*/
		
	}	
	
	
	public ImageGraphics[] getMoneyDigits() {
		String money = Integer.toString(player.getInventory().getMoney());
		ImageGraphics[] digits = new ImageGraphics[money.length()];
		
		for (int i = 0; i < money.length(); i++) {
			switch (money.charAt(i)) {
				case '1':
					digits[i] = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1f, 1f, new RegionOfInterest(0, 0, 16, 16), anchor.add(new Vector(1.3f + 0.75f*i, 0.55f)), 1, 0f);
					break;
				case '2':
					digits[i] = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1f, 1f, new RegionOfInterest(16, 0, 16, 16), anchor.add(new Vector(1.3f + 0.75f*i, 0.55f)), 1, 0f);
					break;
				case '3':
					digits[i] = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1f, 1f, new RegionOfInterest(32, 0, 16, 16), anchor.add(new Vector(1.3f + 0.75f*i, 0.55f)), 1, 0f);
					break;
				case '4':
					digits[i] = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1f, 1f, new RegionOfInterest(48, 0, 16, 16), anchor.add(new Vector(1.3f + 0.75f*i, 0.55f)), 1, 0f);
					break;
				case '5':
					digits[i] = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1f, 1f, new RegionOfInterest(0, 16, 16, 16), anchor.add(new Vector(1.3f + 0.75f*i, 0.55f)), 1, 0f);
					break;
				case '6':
					digits[i] = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1f, 1f, new RegionOfInterest(16, 16, 16, 16), anchor.add(new Vector(1.3f + 0.75f*i, 0.55f)), 1, 0f);
					break;
				case '7':
					digits[i] = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1f, 1f, new RegionOfInterest(32, 16, 16, 16), anchor.add(new Vector(1.3f + 0.75f*i, 0.55f)), 1, 0f);
					break;
				case '8':
					digits[i] = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1f, 1f, new RegionOfInterest(48, 16, 16, 16), anchor.add(new Vector(1.3f + 0.75f*i, 0.55f)), 1, 0f);
					break;
				case '9':
					digits[i] = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1f, 1f, new RegionOfInterest(0, 32, 16, 16), anchor.add(new Vector(1.3f + 0.75f*i, 0.55f)), 1, 0f);
					break;
				case '0':
					digits[i] = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1f, 1f, new RegionOfInterest(16, 32, 16, 16), anchor.add(new Vector(1.3f + 0.75f*i, 0.55f)), 1, 0f);
					break;
				
			}
		}
		System.out.println(digits.length);
		
		return digits;
	}
}
