package sprites;

import engine.direction;
import engine.gameEngine;
import engine.mathVector;

@SuppressWarnings("serial")
public class player extends baseSprite
{
	/*
	 * Class: 			player 
	 * Author: 			Patrick
	 */
	
	public player(int startingX, int startingY)
	{
		super(startingX, startingY);
	}
	
	public player(int startingX, int startingY, int drawingX, int drawingY, int movementDistance) 
	{
		super(startingX, startingY, drawingX, drawingY, movementDistance);
		this.setProjectileSpawnPosition(new mathVector(this.getPosition().x, (this.getPosition().y - this.getDrawingSize().y/2 - 10)));
	}
	
	public void tick()
	{
		if(!gameEngine.gameObjects.exclusiveMoveOnInput)
			this.move(this.getCurrentDirection(), this.getMovementDistance());
			
				
		this.setHitWall(false);
		this.calcBoundingBox();
		this.ticksSinceFire++;
	}

	public static void move(direction d)
	{
		/*
		 * Method: 			move
		 * Author: 			Patrick
		 * Description: 	This method is called every tick for each sprite and handles sprite movement
		 */
		
		switch(d)
		{
			case up:
				gameEngine.gameObjects.player.setPosition(new mathVector(gameEngine.gameObjects.player.getPosition().x, gameEngine.gameObjects.player.getPosition().y - gameEngine.gameObjects.player.getMovementDistance()));
				break;
			case right:
				gameEngine.gameObjects.player.setPosition(new mathVector(gameEngine.gameObjects.player.getPosition().x + gameEngine.gameObjects.player.getMovementDistance(), gameEngine.gameObjects.player.getPosition().y));
				break;
			case down:
				gameEngine.gameObjects.player.setPosition(new mathVector(gameEngine.gameObjects.player.getPosition().x, gameEngine.gameObjects.player.getPosition().y + gameEngine.gameObjects.player.getMovementDistance()));
				break;
			case left:
				gameEngine.gameObjects.player.setPosition(new mathVector(gameEngine.gameObjects.player.getPosition().x - gameEngine.gameObjects.player.getMovementDistance(), gameEngine.gameObjects.player.getPosition().y));
				break;
			default:
				System.err.println("Invalid direction passed to move()");
				break;	
		}
	}
}
