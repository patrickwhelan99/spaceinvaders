package sprites.menu;

import engine.gameEngine;
import engine.level;
import engine.mathVector;

public class menuItemProjectileLimit extends menuItem 
{
	/*
	 * Class: 			menuItemProjectileLimit 
	 * Author: 			Patrick
	 */
	
	public menuItemProjectileLimit(mathVector drawingSize, mathVector drawingPosition) 
	{
		super(drawingSize, drawingPosition);
		this.setPosition(drawingPosition);
		this.setDrawingSize(drawingSize);
	}
	
	
	public void onHit()
	{
		gameEngine.gameObjects.projectileLimit = !gameEngine.gameObjects.projectileLimit;
		if(gameEngine.gameObjects.projectileLimit)
			this.spriteImage = gameEngine.gameObjects.textures.get(17); // checked box
		else
			this.spriteImage = gameEngine.gameObjects.textures.get(18); // Unchecked box
	}

}
