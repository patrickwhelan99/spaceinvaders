package engine;

import sprites.baseSprite;
import sprites.enemy;
import sprites.player;
import sprites.projectile;
import sprites.wall;
import sprites.menu.menuItem;
import sprites.pickup.pickup;
import sprites.pickup.shield;

public class collisionChecker
{
	/*
	 * Class: 			collisionChecker 
	 * Author: 			Patrick
	 */
	
	public collisionChecker()
	{
		
	}
	
	public boolean checkAxis(mathVector normalAxis, baseSprite spriteOne, baseSprite spriteTwo)
	{
		/*
		 * Method: 			checkAxis 
		 * Author: 			Patrick
		 * Description:		The sprites bounding boxes are projected onto the given axies (the boxes normals) and checked for collisions
		 */
		
		mathVector axis = normalAxis; // The axis we're projecting to
		
		/*	Determine minimum and maximum values in relation to the axis for box one */
		double minimumProjectionBoxOne = spriteOne.getBoundingBox().get(1).dotProduct(axis);
		double maximumProjectionBoxOne = spriteOne.getBoundingBox().get(1).dotProduct(axis);
		for(int i=2;i<spriteOne.getBoundingBox().size(); i++)
		{
			if(spriteOne.getBoundingBox().get(i).dotProduct(axis) < minimumProjectionBoxOne)
			{
				minimumProjectionBoxOne = spriteOne.getBoundingBox().get(i).dotProduct(axis);
			}
			
			if(spriteOne.getBoundingBox().get(i).dotProduct(axis) > maximumProjectionBoxOne)
			{
				maximumProjectionBoxOne = spriteOne.getBoundingBox().get(i).dotProduct(axis);
			}
		}
		
		
		/*	Determine minimum and maximum values in relation to the axis for box two */
		double minimumProjectionBoxTwo = spriteTwo.getBoundingBox().get(1).dotProduct(axis);
		double maximumProjectionBoxTwo = spriteTwo.getBoundingBox().get(1).dotProduct(axis);
		for(int i=2;i<spriteTwo.getBoundingBox().size(); i++)
		{
			// If less-than they're intersecting, if equal they're touching
			if(spriteTwo.getBoundingBox().get(i).dotProduct(axis) <= minimumProjectionBoxTwo)
			{
				minimumProjectionBoxTwo = spriteTwo.getBoundingBox().get(i).dotProduct(axis);
			}
			
			if(spriteTwo.getBoundingBox().get(i).dotProduct(axis) >= maximumProjectionBoxTwo)
			{
				maximumProjectionBoxTwo = spriteTwo.getBoundingBox().get(i).dotProduct(axis);
			}
		}
		
		if(maximumProjectionBoxTwo < minimumProjectionBoxOne || maximumProjectionBoxOne < minimumProjectionBoxTwo)
			return false;
		
		return true;
	}
	
	
	
	synchronized public boolean checkCollision(baseSprite spriteOne, baseSprite spriteTwo)
	{
		/*
		 * Method: 		checkCollision 
		 * Author: 			Patrick
		 * Description:		The function to be called externally, combining all other functions in this class to form the game's collision detection system
		 */
		
		/*
		 * 		Check Collision works on the Seperated Axis Theorom (SAT)
		 * 		Based on the work from here: https://gamedevelopment.tutsplus.com/tutorials/collision-detection-using-the-separating-axis-theorem--gamedev-169
		 */
		
		
		boolean collision = false;
		
		// Check for any combinations of sprites whose collisions don't need to be checked, excepting them from the remainder of the function
		if(!checkForExceptors(spriteOne, spriteTwo))
			return false;
				
		
		// Get normals for the two boxes defined as (-dy, dx) for the left normal. In a 2D grid only 2 axes needed as boxes will always be oriented the same way
		mathVector boxOneNormalOne = new mathVector(-(spriteOne.getBoundingBox().get(1).y - spriteOne.getBoundingBox().get(2).y), spriteOne.getBoundingBox().get(1).x - spriteOne.getBoundingBox().get(2).x);
		mathVector boxOneNormalTwo = new mathVector(-(spriteOne.getBoundingBox().get(1).y - spriteOne.getBoundingBox().get(3).y), spriteOne.getBoundingBox().get(1).x - spriteOne.getBoundingBox().get(3).x);
		
		mathVector boxTwoNormalOne = new mathVector(-(spriteTwo.getBoundingBox().get(1).y - spriteTwo.getBoundingBox().get(2).y), spriteTwo.getBoundingBox().get(1).x - spriteTwo.getBoundingBox().get(2).x);
		mathVector boxTwoNormalTwo = new mathVector(-(spriteTwo.getBoundingBox().get(1).y - spriteTwo.getBoundingBox().get(3).y), spriteTwo.getBoundingBox().get(1).x - spriteTwo.getBoundingBox().get(3).x);
		
		boolean b1n1 = checkAxis(boxOneNormalOne, spriteOne, spriteTwo);
		boolean b1n2 = checkAxis(boxOneNormalTwo, spriteTwo, spriteOne);
		
		boolean b2n1 = checkAxis(boxTwoNormalOne, spriteOne, spriteTwo);
		boolean b2n2 = checkAxis(boxTwoNormalTwo, spriteTwo, spriteOne);
		
		// A collision must be detected all (both) axies
		if(b1n1 && b1n2 || b2n1 && b2n2)
			collision = true;
		
		if (collision) 
		{
			// Called twice to avoid repetition of code
			handleWalls(spriteOne, spriteTwo);
			handleWalls(spriteTwo, spriteOne);
			
			if(spriteOne instanceof pickup && spriteTwo instanceof player)
			{
				pickup pu = (pickup)spriteOne;
				pu.onPickup();
				return false;
			}
			if(spriteTwo instanceof pickup && spriteOne instanceof player)
			{
				pickup pu = (pickup)spriteTwo;
				pu.onPickup();
				return false;
			}
			
			if(spriteOne instanceof menuItem && spriteTwo instanceof projectile)
			{
				menuItem m = (menuItem)spriteOne;
				m.onHit();
				gameEngine.gameObjects.spritesToRemove.add(spriteTwo);
				return false;
			}
			
			if(spriteTwo instanceof menuItem && spriteOne instanceof projectile)
			{
				menuItem m = (menuItem)spriteTwo;
				m.onHit();
				gameEngine.gameObjects.spritesToRemove.add(spriteOne);
				return false;
			}		
		}
		
		return collision;
		
	}
	
	public void handleWalls(baseSprite spriteOne, baseSprite spriteTwo)
	{

		/*
		 *	Method: handleWalls
		 *	Author: Patrick
		 *	Description:	Checks for sprite collision with a wall and handles movement accordingly 
		 */
		
		
		// First a check is made if a wall is involved in the collision
		if(spriteOne instanceof wall)
		{
			wall w = (wall) spriteOne;	// Down-cast to a wall
			
			// Then determine which wall we have hit
			if (w.getPositionalDirection() == direction.left) 
			{
				spriteTwo.setCurrentDirection(direction.right);
				
				if(spriteTwo instanceof enemy)
				{
					for(enemy en : gameEngine.gameObjects.enemies)
					{
						en.setCurrentDirection(direction.right);
						en.move(direction.down, gameEngine.gameObjects.decensionSpeed);
						en.setHitWall(false);
					}
				}
				
				spriteTwo.setHitWall(true);
			}

			else if (w.getPositionalDirection() == direction.right) 
			{
				spriteTwo.setCurrentDirection(direction.left);
			
				if(spriteTwo instanceof enemy)
				{
					for(enemy en : gameEngine.gameObjects.enemies)
					{
						en.setCurrentDirection(direction.left);
						en.move(direction.down, gameEngine.gameObjects.decensionSpeed);
						en.setHitWall(false);
					}
				}
				spriteTwo.setHitWall(true);
			}
			else if(w.getPositionalDirection() == direction.down)
			{
				spriteTwo.setHitWall(true);
				if(spriteTwo instanceof enemy)
					gameEngine.endGame(false);
			}
		}
	}
	
	boolean checkForExceptors(baseSprite spriteOne, baseSprite spriteTwo)
	{
		/*
		 * Method: checkForExceptors
		 * Author: Patrick
		 * Description:	Check for any combinations of sprites whose collisions don't do anything
		 */
		
		if(spriteOne instanceof shield && spriteTwo instanceof player)
		{
			return false;
		}
		if(spriteTwo instanceof shield && spriteOne instanceof player)
		{
			return false;
		}
		
		if(spriteOne instanceof pickup)
		{
			pickup pu = (pickup)spriteOne;
			if(pu.isPickedUp())
				return false;
		}
		if(spriteTwo instanceof pickup)
		{
			pickup pu = (pickup)spriteTwo;
			if(pu.isPickedUp())
				return false;
		}
		
		
		if(spriteOne instanceof shield && spriteTwo instanceof projectile)
		{
			projectile p = (projectile)spriteTwo;	
			if(p.getShooter() == gameEngine.gameObjects.player)
				return false;
		}
		if(spriteTwo instanceof shield && spriteOne instanceof projectile)
		{
			projectile p = (projectile)spriteOne;	
			if(p.getShooter() == gameEngine.gameObjects.player)
				return false;
		}
		
		if(spriteOne instanceof shield && !(spriteTwo  instanceof projectile) || spriteTwo instanceof shield && !(spriteOne instanceof projectile))
		{
				return false;
		}
		
		if(spriteOne instanceof projectile)
		{
			projectile p = (projectile)spriteOne;
			if(p.getShooter().equals(spriteTwo) || gameEngine.gameObjects.projectiles.contains(spriteTwo))
					return false;	
		}
		
		if(spriteTwo instanceof projectile)
		{
			projectile p = (projectile)spriteTwo;
			if(p.getShooter().equals(spriteTwo) || gameEngine.gameObjects.projectiles.contains(spriteTwo))
				return false;
		}
		
		if(spriteOne instanceof menuItem && spriteTwo instanceof player || spriteTwo instanceof menuItem && spriteOne instanceof player)
		{
			return false;
		}
			
		return true;
	}
}
