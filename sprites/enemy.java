package sprites;

import java.awt.Image;
import java.util.concurrent.ThreadLocalRandom;

import engine.gameEngine;
import engine.gameObjects;
import engine.mathVector;

@SuppressWarnings("serial")
public class enemy extends baseSprite
{
	/*
	 * Class: 			enemy 
	 * Author: 			Patrick
	 */
	
	private baseSprite friendlyChecker = new baseSprite(0, 0);
	private boolean canFire = false;
	
	
	
	public enemy(Image texture) 
	{
		super(texture);
	}
	
	public enemy(Image texture, int startingX, int startingY, int drawingX, int drawingY, int movementDistance) 
	{
		super(texture, startingX, startingY, drawingX, drawingY, movementDistance);
	}
	
	public enemy(int startingX, int startingY) 
	{
		super(startingX, startingY);
	}
	
	public enemy(int startingX, int startingY, int drawingX, int drawingY) 
	{
		super(startingX, startingY, drawingX, drawingY);
	}
	
	@Override
	public void calcBoundingBox()
	{
		this.boundingBox.clear();
		this.calcCentre();
		this.boundingBox.add(getCentre());
		this.boundingBox.add(new mathVector(this.getPosition().x, this.getPosition().y)); // Upper left vertex
		this.boundingBox.add(new mathVector(this.getPosition().x + this.getDrawingSize().x, this.getPosition().y)); // Upper right vertex
		this.boundingBox.add(new mathVector(this.getPosition().x, this.getPosition().y + this.getDrawingSize().y)); // Bottom left vertex
		this.boundingBox.add(new mathVector(this.getPosition().x + this.getDrawingSize().x, this.getPosition().y + this.getDrawingSize().y)); // Bottom right vertex
		
		/*Calc box checking if can fire downwards without hitting another enemy*/
		this.friendlyChecker.boundingBox.clear();
		this.getFriendlyChecker().boundingBox.add(new mathVector(this.getBoundingBox().get(0).x, this.getBoundingBox().get(0).y));
		this.getFriendlyChecker().boundingBox.add(new mathVector(this.getBoundingBox().get(1).x, this.getBoundingBox().get(1).y));
		this.getFriendlyChecker().boundingBox.add(new mathVector(this.getBoundingBox().get(2).x, this.getBoundingBox().get(2).y));
		this.getFriendlyChecker().boundingBox.add(new mathVector(this.getBoundingBox().get(1).x, 1000));
		this.getFriendlyChecker().boundingBox.add(new mathVector(this.getBoundingBox().get(2).x, 1000));
	}
	
	@Override
	public void tick()
	{
		this.ticksSinceMove++;
		this.ticksSinceFire++;
		
		
		if(this.getCanFire())
		{
			int randNum = ThreadLocalRandom.current().nextInt(0, 100 + 1); // Random number between 0 and 100
			if(randNum <= gameEngine.gameObjects.enemyChanceToFire)
			{
				this.setProjectileSpawnPosition(new mathVector(this.getPosition().x, (this.getPosition().y + this.getDrawingSize().y/2 + 10)));
				fireProjectile(engine.direction.down);
				
				this.canFire = false;
				this.setTicksSinceFire(0);
			}
		}
		
		if(this.isHitWall())
		{
			for(enemy e : gameEngine.gameObjects.enemies)
			{
				e.move(this.getCurrentDirection(), this.getMovementDistance());
				e.calcBoundingBox();
				e.setHitWall(false);
			}
			return;
		}
		
		if(this.getTicksSinceMove() < 24)
			return;
		 
		this.move(this.getCurrentDirection(), this.getMovementDistance());
		this.calcBoundingBox();
		this.setTicksSinceMove(0);
	}

	public baseSprite getFriendlyChecker() {
		return friendlyChecker;
	}

	public void setFriendlyChecker(baseSprite friendlyChecker) {
		this.friendlyChecker = friendlyChecker;
	}

	public boolean getCanFire() {
		return canFire;
	}

	public void setCanFire(boolean canFire) {
		this.canFire = canFire;
	}
	
	
}
