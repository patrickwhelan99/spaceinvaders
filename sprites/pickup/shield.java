package sprites.pickup;

import java.awt.Image;

import engine.gameEngine;
import engine.gameObjects;
import engine.mathVector;
import sprites.baseSprite;

@SuppressWarnings("serial")
public class shield extends baseSprite
{
	/*
	 * Class: 			shield 
	 * Author: 			Patrick
	 */
	
	public shield(Image texture, int startingX, int startingY, int drawingX, int drawingY, int movementDistance)
	{
		super(texture, startingX, startingY, drawingX, drawingY, movementDistance);
	} 
	
	public void calcBoundingBox()
	{
		this.boundingBox.clear();
		this.boundingBox.add(gameEngine.gameObjects.player.getCentre());
		this.boundingBox.add(new mathVector(gameEngine.gameObjects.player.getCentre().x - this.getDrawingSize().x/2, gameEngine.gameObjects.player.getCentre().y - this.getDrawingSize().y/2)); // Upper left vertex
		this.boundingBox.add(new mathVector(gameEngine.gameObjects.player.getCentre().x + this.getDrawingSize().x/2, gameEngine.gameObjects.player.getCentre().y - this.getDrawingSize().y/2)); // Upper right vertex
		this.boundingBox.add(new mathVector(gameEngine.gameObjects.player.getCentre().x - this.getDrawingSize().x/2, gameEngine.gameObjects.player.getCentre().y + this.getDrawingSize().y/2)); // Bottom left vertex
		this.boundingBox.add(new mathVector(gameEngine.gameObjects.player.getCentre().x + this.getDrawingSize().x/2, gameEngine.gameObjects.player.getCentre().y + this.getDrawingSize().y/2)); // Bottom right vertex
	}
		
	public void tick()
	{
		this.setPosition(new mathVector(gameEngine.gameObjects.player.getPosition().x - gameEngine.gameObjects.player.getDrawingSize().x/2 - 25, gameEngine.gameObjects.player.getPosition().y - gameEngine.gameObjects.player.getDrawingSize().y/2 - 25));
		this.calcBoundingBox();
	}

}
