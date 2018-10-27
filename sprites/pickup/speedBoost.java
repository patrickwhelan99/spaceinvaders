package sprites.pickup;

import java.awt.Image;

import engine.gameEngine;
import engine.gameObjects;

@SuppressWarnings("serial")
public class speedBoost extends pickup
{
	/*
	 * Class: 			speedBoost 
	 * Author: 			Patrick
	 */
	
	private float speedModifier = 3.0f;
	
	public speedBoost(Image texture, int startingX, int startingY, int drawingX, int drawingY, int ticksToLive)
	{
		super(texture, startingX, startingY, drawingX, drawingY, ticksToLive);
	}
	
	public void onPickup()
	{
		this.setPickedUp(true);
		this.setTicksToLive(50); // time for effect to last
		gameEngine.gameObjects.player.setMovementDistance((int)(gameEngine.gameObjects.player.getMovementDistance()*speedModifier));
		
		gameEngine.playSound("pickup", false);
	}
	
	public void destroy()
	{
		if(isPickedUp())
			gameEngine.gameObjects.player.setMovementDistance((int)(gameEngine.gameObjects.player.getMovementDistance()/speedModifier));
		gameEngine.gameObjects.allSprites.remove(this);
	}
}
