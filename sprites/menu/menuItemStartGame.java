package sprites.menu;

import engine.gameEngine;
import engine.level;
import engine.mathVector;
import sprites.baseSprite;

public class menuItemStartGame extends menuItem 
{
	/*
	 * Class: 			menuItemStartGame 
	 * Author: 			Patrick
	 */
	
	public menuItemStartGame(mathVector drawingSize, mathVector drawingPosition) 
	{
		super(drawingSize, drawingPosition);
		this.setPosition(drawingPosition);
		this.setDrawingSize(drawingSize);
	}
	
	
	public void onHit()
	{
		if(!gameEngine.gameObjects.fullMovement)
			gameEngine.gameObjects.player.setPosition(new mathVector(gameEngine.gameObjects.player.getPosition().x, 950*gameEngine.gameObjects.screenScale.y));
		level.setLevelNum(0);
		for(baseSprite bs : gameEngine.gameObjects.allSprites)
			if(bs instanceof menuItem)
				gameEngine.gameObjects.allSprites.remove(bs);
	}

}
