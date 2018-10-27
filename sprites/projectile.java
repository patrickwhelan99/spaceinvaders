package sprites;

import java.awt.Image;

import engine.direction;
import engine.gameEngine;
import engine.gameObjects;
import engine.mathVector;

@SuppressWarnings("serial")
public class projectile extends baseSprite
{
	/*
	 * Class: 			projectile 
	 * Author: 			Patrick
	 */
	
	private baseSprite shooter; // reference to shooter kept to prevent shooter being killed by self
	
	public projectile(Image spriteImage, mathVector projectileSpawnPosition, direction startingDirection, int drawingX, int drawingY, baseSprite shooter, int movementDistance)
	{
		super(spriteImage, (int)projectileSpawnPosition.x, (int)projectileSpawnPosition.y, drawingX, drawingY, movementDistance);
		this.setCurrentDirection(startingDirection);
		this.setShooter(shooter);   
	}

	public baseSprite getShooter() {
		return shooter;
	}

	private void setShooter(baseSprite shooter) {
		this.shooter = shooter;
	}
	
	public void tick()
	{
		this.setHitWall(false);
		this.move(this.getCurrentDirection(), this.getMovementDistance());
		if(this.getPosition().y < 0 || this.getPosition().y > 2000) // delete if off-screen to improve performance
			gameEngine.gameObjects.spritesToRemove.add(this);
		this.calcBoundingBox();
	}

}
