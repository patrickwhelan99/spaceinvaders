package sprites.menu;

import engine.gameEngine;
import engine.mathVector;

public class menuItemMovementDirections extends menuItem
{
	/*
	 * Class: 			menuItemMovementDirections 
	 * Author: 			Patrick
	 */
	
	public menuItemMovementDirections(mathVector drawingSize, mathVector drawingPosition) 
	{
		super(drawingSize, drawingPosition);
		this.setPosition(drawingPosition);
		this.setDrawingSize(drawingSize);
	}
	
	
	public void onHit()
	{
		gameEngine.gameObjects.fullMovement = !gameEngine.gameObjects.fullMovement;
		if(gameEngine.gameObjects.fullMovement)
			this.spriteImage = gameEngine.gameObjects.textures.get(17); // checked box
		else
			this.spriteImage = gameEngine.gameObjects.textures.get(18); // Unchecked box
	}

}
