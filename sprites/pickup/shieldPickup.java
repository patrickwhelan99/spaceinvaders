package sprites.pickup;

import java.awt.Image;

import engine.gameEngine;
import engine.gameObjects;
import sprites.baseSprite;

@SuppressWarnings("serial")
public class shieldPickup extends pickup
{
	/*
	 * Class: 			shieldPickup 
	 * Author: 			Patrick
	 */
	
	public shieldPickup(Image texture, int startingX, int startingY, int drawingX, int drawingY, int ticksToLive)
	{
		super(texture, startingX, startingY, drawingX, drawingY, ticksToLive);
	}
	
	public void onPickup()
	{
		this.setPickedUp(true);
		this.setTicksToLive(100000); // time for pickup to last 
		shield s = new shield(spriteImage, (int)gameEngine.gameObjects.player.getBoundingBox().get(1).x, (int)gameEngine.gameObjects.player.getBoundingBox().get(1).y, 150, 150, gameEngine.gameObjects.player.getMovementDistance());
		s.calcBoundingBox();
		gameEngine.gameObjects.allSprites.add(s);
		
		gameEngine.playSound("pickup", false);
	}
	
	public void destroy()
	{
		if(isPickedUp())
		{
			for(baseSprite bs : gameEngine.gameObjects.allSprites)
			{
				if(bs instanceof shield)
					gameEngine.gameObjects.allSprites.remove(bs);
			}
		}
			
		gameEngine.gameObjects.allSprites.remove(this);
	}

}
