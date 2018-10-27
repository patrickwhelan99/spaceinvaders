package sprites.menu;

import engine.gameEngine;
import engine.level;
import engine.mathVector;

public class menuItemExclusiveMoveOnInput extends menuItem 
{
	/*
	 * Class: 			menuItemExclusiveMoveOnInput 
	 * Author: 			Patrick
	 */
	
	public menuItemExclusiveMoveOnInput(mathVector drawingSize, mathVector drawingPosition) 
	{
		super(drawingSize, drawingPosition);
		this.setPosition(drawingPosition);
		this.setDrawingSize(drawingSize);
	}
	
	
	public void onHit()
	{
		gameEngine.gameObjects.exclusiveMoveOnInput = !gameEngine.gameObjects.exclusiveMoveOnInput;
		if(gameEngine.gameObjects.exclusiveMoveOnInput)
			this.spriteImage = gameEngine.gameObjects.textures.get(17); // checked box
		else
			this.spriteImage = gameEngine.gameObjects.textures.get(18); // Unchecked box
	}

}
