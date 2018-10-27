package sprites.menu;

import engine.gameEngine;
import engine.level;
import engine.mathVector;

public class menuItemLevelIncrease extends menuItem 
{
	/*
	 * Class: 			menuItemLevelIncrease 
	 * Author: 			Patrick
	 */
	
	public menuItemLevelIncrease(mathVector drawingSize, mathVector drawingPosition) 
	{
		super(drawingSize, drawingPosition);
		this.setPosition(drawingPosition);
		this.setDrawingSize(drawingSize);
	}
	
	
	public void onHit()
	{
		gameEngine.gameObjects.maxLevels++;
	}

}
