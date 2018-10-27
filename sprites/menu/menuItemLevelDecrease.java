package sprites.menu;

import engine.gameEngine;
import engine.level;
import engine.mathVector;

public class menuItemLevelDecrease extends menuItem 
{
	/*
	 * Class: 			menuItemLevelDecrease 
	 * Author: 			Patrick
	 */
	
	public menuItemLevelDecrease(mathVector drawingSize, mathVector drawingPosition) 
	{
		super(drawingSize, drawingPosition);
		this.setPosition(drawingPosition);
		this.setDrawingSize(drawingSize);
	}
	
	
	public void onHit()
	{
		if(gameEngine.gameObjects.maxLevels > 1)
			gameEngine.gameObjects.maxLevels--;
	}

}
