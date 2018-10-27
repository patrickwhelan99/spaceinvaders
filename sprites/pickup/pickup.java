package sprites.pickup;

import java.awt.Image;

import engine.gameEngine;
import engine.gameObjects;
import sprites.baseSprite;

@SuppressWarnings("serial")
public class pickup extends baseSprite
{
	/*
	 * Class: 			pickup 
	 * Author: 			Patrick
	 */
	
	private int ticksToLive;
	private boolean pickedUp = false;
	private boolean shouldDraw = true;
	
	
	public pickup(Image texture, int startingX, int startingY, int drawingX, int drawingY, int ticksToLive) 
	{
		super(texture, startingX, startingY, drawingX, drawingY, 0);
		this.setTicksToLive(ticksToLive);
		this.calcBoundingBox();
	}

	public void onPickup(){}; // To be overridden
	
	public void destroy(){}; // To be overridden
	
	public void tick()
	{
		this.calcBoundingBox();
		
		this.ticksToLive--;
		if(this.ticksToLive < 1)
		{
			this.destroy();
		}
		
		if(this.ticksToLive < gameEngine.gameObjects.tickSpeed)
		{
			this.setShouldDraw(!this.ShouldDraw()); // Alternate drawing making the pickup flash if time to live is less than 1 seconds
		}
	}
	
	public int getTicksToLive()
	{
		return ticksToLive;
	}

	public void setTicksToLive(int ticksToLive)
	{
		this.ticksToLive = ticksToLive;
	}

	public boolean isPickedUp()
	{
		return pickedUp;
	}

	public void setPickedUp(boolean pickedUp)
	{
		this.pickedUp = pickedUp;
	}

	public boolean ShouldDraw() {
		return shouldDraw;
	}

	public void setShouldDraw(boolean shouldDraw) {
		this.shouldDraw = shouldDraw;
	}

}
