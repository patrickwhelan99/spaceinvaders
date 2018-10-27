package sprites.pickup;

import java.awt.Image;

import engine.gameEngine;
import engine.gameObjects;

@SuppressWarnings("serial")
public class fireRateBoost extends pickup
{
	/*
	 * Class: 			fireRateBoost 
	 * Author: 			Patrick
	 */
	
	private float tickMinFireRateModifier = 0.5f;
	
	public fireRateBoost(Image texture, int startingX, int startingY, int drawingX, int drawingY, int ticksToLive)
	{
		super(texture, startingX, startingY, drawingX, drawingY, ticksToLive);
	}
	
	public void onPickup()
	{
		this.setPickedUp(true);
		this.setTicksToLive(100); // time for effect to last
		gameEngine.gameObjects.player.setTickMinFire((int)(gameEngine.gameObjects.player.getTickMinFire()*tickMinFireRateModifier));
		gameEngine.gameObjects.player.setTicksSinceFire(0);
		
		gameEngine.playSound("pickup", false);
	}
	
	public void destroy()
	{
		if(isPickedUp())
			gameEngine.gameObjects.player.setTickMinFire((int)(gameEngine.gameObjects.player.getTickMinFire()/tickMinFireRateModifier));
		gameEngine.gameObjects.allSprites.remove(this);                 
	}
}
