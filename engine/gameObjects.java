package engine;

import java.awt.Font;
import java.awt.Image;
import java.util.concurrent.CopyOnWriteArrayList;

import sprites.baseSprite;
import sprites.enemy;
import sprites.healthBar;
import sprites.projectile;
import sprites.wall;
import sprites.menu.menuItem;
import sprites.menu.menuItemExclusiveMoveOnInput;
import sprites.menu.menuItemLevelDecrease;
import sprites.menu.menuItemLevelIncrease;
import sprites.menu.menuItemMovementDirections;
import sprites.menu.menuItemProjectileLimit;
import sprites.menu.menuItemStartGame;

public class gameObjects
{
	/*
	 * Class: 			gameObjects 
	 * Author: 			Patrick
	 */
	
	public mathVector screenScale;
	public CopyOnWriteArrayList<baseSprite> allSprites;
	public CopyOnWriteArrayList<enemy> enemies;
	public CopyOnWriteArrayList<Image> textures;
	public sprites.player player;
	public sprites.boss boss;
	public long tickSpeed; //1000/tickspeed=framerate
	public int decensionSpeed;
	public int enemyChanceToFire; // If random number between 0 and 100 is equal to or below this fire. Acts as a percentage chance
	public int powerupSpawnChance;
	public CopyOnWriteArrayList<projectile> projectiles;
	public CopyOnWriteArrayList<baseSprite> spritesToRemove;
	public wall rightWall;
	public wall leftWall;
	public wall bottomWall;
	public int maxLevels;
	public healthBar bossHealthBar;
	public Font spaceFont;
	public boolean fullMovement;
	public menuItemMovementDirections fullMovementMenuItem;
	public menuItemStartGame startGameMenuItem;
	public menuItemProjectileLimit projectileLimitMenuItem;
	public boolean projectileLimit;
	public boolean exclusiveMoveOnInput;
	public menuItemExclusiveMoveOnInput exclusiveMoveOnInputMenuItem;
	public menuItemLevelIncrease levelIncreaseMenuItem;
	public menuItemLevelDecrease levelDecreaseMenuItem;
	public boolean endGameCalled;
	
	gameObjects()
	{
		
	}
}    
 
