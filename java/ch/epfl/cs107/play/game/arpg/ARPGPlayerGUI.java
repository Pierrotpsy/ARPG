package ch.epfl.cs107.play.game.arpg;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class ARPGPlayerGUI implements Graphics{
	private float width;
	private float height;
	private Vector anchor;
	
	@Override
	public void draw(Canvas canvas) {
	
	width = canvas.getScaledWidth();
	height = canvas.getScaledHeight();
	
	anchor = canvas.getTransform().getOrigin().sub(new Vector(width/2, height/2)); 
	ImageGraphics gearDisplay = new ImageGraphics(ResourcePath.getSprite("zelda/gearDisplay"), 1.5f, 1.5f, new RegionOfInterest(0, 0, 32, 32), anchor.add(new Vector(0, height - 1.75f)), 1, 0f);
	gearDisplay.draw(canvas);		
	}

}
