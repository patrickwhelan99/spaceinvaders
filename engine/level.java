package engine;

import java.util.concurrent.ThreadLocalRandom;

import sprites.baseSprite;
import sprites.boss;
import sprites.enemy;

public class level
{
	/*
	 * Class: 			level 
	 * Author: 			Patrick
	 */
	
	private static int enemies = 0;
	private static int enemiesAtStart = 0;
	private static int levelNum = 0;
	
	public static int checkForWinner()
	{
		if(gameEngine.gameObjects.enemies.size() <= 0 && level.levelNum != gameEngine.gameObjects.maxLevels)
			return 0;
		if(level.levelNum >= gameEngine.gameObjects.maxLevels && gameEngine.gameObjects.boss.isDestroyed())
			return 1;
		if(!gameEngine.gameObjects.allSprites.contains(gameEngine.gameObjects.player))
			return 2;

		return 3;
	}


	public static void newLevel(int enemies) 
	{
		
		
		
		if(levelNum < 0)
		{
			return;
		}
		
		level.setLevelNum(level.getLevelNum()+1);
		
		
		if(level.getLevelNum() == gameEngine.gameObjects.maxLevels) // Final boss level
		{
			level.setEnemies(0);
			for(baseSprite e : gameEngine.gameObjects.enemies)
				gameEngine.gameObjects.allSprites.remove(e);
			gameEngine.gameObjects.enemies.clear();
			
			gameEngine.gameObjects.boss.setPosition(new mathVector(750*gameEngine.gameObjects.screenScale.x, 250*gameEngine.gameObjects.screenScale.y));
			
			gameEngine.gameObjects.bossHealthBar.setEnabled(true);
			
			gameEngine.playSound("boss", true); // boss level
			
			return;
		}
		
		level.enemiesAtStart = enemies;
		level.enemies = enemies;
		
		
		for(int j=0;j<(enemies/15)+1;j++)
		{
			int limit = (enemies-j*15 < 15)  ? (enemies-j*15)%15 : 15;
			for(int i=0;i<limit;i++)
			{
				int randNum = ThreadLocalRandom.current().nextInt(3, 5 + 1);				
				if(j%2 == 0)
					gameEngine.gameObjects.enemies.add(new enemy(gameEngine.gameObjects.textures.get(randNum), i*100 + 100, j*75 + 100, 50, 50, 75));
				else
					gameEngine.gameObjects.enemies.add(new enemy(gameEngine.gameObjects.textures.get(randNum), i*100 + 150, j*75 + 100, 50, 50, 75));
			} 
		}
		
		
		// Add enemies to allSprites array
		for(baseSprite bs : gameEngine.gameObjects.enemies)
		{
			gameEngine.gameObjects.allSprites.add(bs);
			bs.setCurrentDirection(direction.right);
			bs.calcBoundingBox();
		}
		
		
		
	}
	
	public static int getEnemies() {
		return enemies;
	}

	public static void setEnemies(int enemies) {
		level.enemies = enemies;
	}

	public static int getLevelNum() {
		return levelNum;
	}

	public static void setLevelNum(int levelNum) {
		level.levelNum = levelNum;
		
		switch(levelNum)
		{
			case -10: // Main menu
				gameEngine.playSound("menu", true);
				break;
				
			case -2: // Player loss
				gameEngine.playSound("death", false);
				break;
				
			case -1: // Player win
				gameEngine.playSound("victory", true);
				break;
				
			default:
				gameEngine.playSound("default", true);
				break;
		}
	}

	public static int getEnemiesAtStart() {
		return enemiesAtStart;
	}

	public static void setEnemiesAtStart(int enemiesAtStart) {
		level.enemiesAtStart = enemiesAtStart;
	}
}
