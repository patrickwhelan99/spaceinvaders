package sprites;

import java.awt.Image;
import java.util.concurrent.ThreadLocalRandom;

import engine.direction;
import engine.gameEngine;
import engine.gameObjects;
import engine.level;
import engine.mathVector;

@SuppressWarnings("serial")
public class boss extends baseSprite 
{
	/*
	 * Class: 			boss 
	 * Author: 			Patrick
	 */
	
	public boss(int startingX, int startingY, int drawingX, int drawingY, int movementDistance) 
	{
		super(startingX, startingY, drawingX, drawingY, movementDistance);
		this.setHealth(10.0f);
		this.setTickMinFire(24);
	}
	
	@Override
	public void tick()
	{
		this.setTicksSinceFire(this.getTicksSinceFire() + 1);
		this.setHitWall(false);
		this.move(this.getCurrentDirection(), this.getMovementDistance());
		this.calcBoundingBox();
		this.setProjectileSpawnPosition(new mathVector(this.getPosition().x, (this.getPosition().y + this.getDrawingSize().y/2 + 10)));
		
		if(this.getTicksSinceFire() >= this.getTickMinFire())
		{
			this.fireProjectile(direction.down);
		}
	}
	
	@Override
	synchronized public void fireProjectile(engine.direction directionToFire)
	{
		/*
		 * Method: 			fireProjectile
		 * Author: 			Patrick
		 * Description: 	This method is overridden to provide a special fire for the boss
		 */
				
		if(this.getTicksSinceFire() < this.getTickMinFire() || level.getLevelNum() != gameEngine.gameObjects.maxLevels)
			return;
		
		for(double i=1;i<20;i+=2)
		{
			double offset =  ThreadLocalRandom.current().nextInt(0, 250 + 1);
			projectile newProjectile = new projectile(gameEngine.gameObjects.textures.get(1), new mathVector(this.getProjectileSpawnPosition().x*(i/10) + offset, this.getProjectileSpawnPosition().y), directionToFire, 10, 25, this, this.projectileFireSpeed);
			newProjectile.calcBoundingBox();
			gameEngine.gameObjects.allSprites.add(newProjectile);
			gameEngine.gameObjects.projectiles.add(newProjectile);
		}
		
		this.setTicksSinceFire(0);
		
		gameEngine.playSound("shot", false);
		gameEngine.playSound("shot", false);
		gameEngine.playSound("shot", false);
		gameEngine.playSound("shot", false);
		gameEngine.playSound("shot", false);
		gameEngine.playSound("shot", false);
	}

}
