package engine;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CopyOnWriteArrayList; // Thread-safe arraylist
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import graphics.drawer;
import sprites.baseSprite;
import sprites.boss;
import sprites.enemy;
import sprites.healthBar;
import sprites.player;
import sprites.projectile;
import sprites.wall;
import sprites.menu.menuItemExclusiveMoveOnInput;
import sprites.menu.menuItemLevelDecrease;
import sprites.menu.menuItemLevelIncrease;
import sprites.menu.menuItemMovementDirections;
import sprites.menu.menuItemProjectileLimit;
import sprites.menu.menuItemStartGame;
import sprites.pickup.fireRateBoost;
import sprites.pickup.pickup;
import sprites.pickup.shieldPickup;
import sprites.pickup.speedBoost;

public class gameEngine implements Runnable
{
	/*
	 * Class: 			gameEngine 
	 * Author: 			Patrick
	 */
	
	drawer drawer;
	private collisionChecker collisionChecker = new collisionChecker();
	public boolean destroy = false;
	private Thread worker;
	public static Clip loopingMusic;
	public static gameObjects gameObjects = new gameObjects();
	
	public CopyOnWriteArrayList<Image> loadTextures() throws IOException
	{
		/*
		 * Method: 			loadTextures
		 * Author: 			Patrick
		 * Description: 	Loads textures from the resource folder "graphics" into a thread-safe copyOnWrite ArrayList
		 */
		
		CopyOnWriteArrayList<Image> textures = new CopyOnWriteArrayList <Image>();
		
		BufferedImage img = ImageIO.read(this.getClass().getClassLoader().getResource("graphics/ship.png")); // player sprite | position: 0
		textures.add(img);
		
		img = ImageIO.read(this.getClass().getClassLoader().getResource("graphics/missile.png")); // missile sprite | position: 1
		textures.add(img);
		
		img = ImageIO.read(this.getClass().getClassLoader().getResource("graphics/boss.png")); // boss sprite | position: 2
		textures.add(img);
		
		img = ImageIO.read(this.getClass().getClassLoader().getResource("graphics/aliens/alien.png")); // alien sprite | position: 3
		textures.add(img);
		
		img = ImageIO.read(this.getClass().getClassLoader().getResource("graphics/aliens/alien2.png")); // alternative alien sprite | position: 4
		textures.add(img);
		
		img = ImageIO.read(this.getClass().getClassLoader().getResource("graphics/aliens/alien3.png")); // alternative alien sprite | position: 5
		textures.add(img);
		
		for(int i=1;i<8;i++)
		{
			img = ImageIO.read(this.getClass().getClassLoader().getResource("graphics/explosion/xpl" + i + ".png")); // explosion animation images | positions: 6-12
			textures.add(img);
		}
		
		img = ImageIO.read(this.getClass().getClassLoader().getResource("graphics/background.png")); // Background image | position: 13
		textures.add(img);
		
		img = ImageIO.read(this.getClass().getClassLoader().getResource("graphics/powerups/spacecheese.png")); // cheese image | position: 14
		textures.add(img);
		
		img = ImageIO.read(this.getClass().getClassLoader().getResource("graphics/powerups/spacemunch.png")); // spacemunch image | position: 15
		textures.add(img);
		
		img = ImageIO.read(this.getClass().getClassLoader().getResource("graphics/powerups/shipshield.png")); // shield image | position: 16
		textures.add(img);
		
		img = ImageIO.read(this.getClass().getClassLoader().getResource("graphics/checked.png")); // checked check-box image | position: 17
		textures.add(img);
		
		img = ImageIO.read(this.getClass().getClassLoader().getResource("graphics/unchecked.png")); // unchecked check-box image | position: 18
		textures.add(img);
		
		img = ImageIO.read(this.getClass().getClassLoader().getResource("graphics/upArrow.png")); // up arrow image | position: 19
		textures.add(img);
		
		img = ImageIO.read(this.getClass().getClassLoader().getResource("graphics/downArrow.png")); // down arrow image | position: 20
		textures.add(img);
		
		return textures;
	}
	
	synchronized public void removeSprites(CopyOnWriteArrayList<baseSprite> spritesToRemove)
	{
		/*
		 * Method: 			removeSprites
		 * Author: 			Patrick
		 * Description: 	Removes all sprites in the given arraylist from the game entirely
		 */
		
		for(baseSprite s : spritesToRemove)
		{
			s.setDestroyed(true);
			gameObjects.allSprites.remove(s);
			
			
			if(s instanceof enemy)
					gameObjects.enemies.remove(s);
			
			
			if(s instanceof projectile)
				gameObjects.projectiles.remove(s);
			
						
			if(s instanceof pickup)
			{
				pickup pu = (pickup)s;
				pu.destroy();
			}
			
			if(s instanceof boss)
			{
				boss b = (boss) s;
				b.setDestroyed(true);
			}
			
			gameObjects.spritesToRemove.clear();
		}	
	}
	
	public void spawnPickups()
	{
		/*
		 * Method: 			spawnPickups
		 * Author: 			Patrick
		 * Description: 	Called every loop (frame) it has a 1% chance (variable) of spawning a pickUp for the player
		 */
		
		int randNum = ThreadLocalRandom.current().nextInt(0, 100 + 1);
		if(randNum < gameObjects.powerupSpawnChance)
		{
			int randX = ThreadLocalRandom.current().nextInt(200, 1700 + 1);
			int randTicksToLive = ThreadLocalRandom.current().nextInt(50, 200 + 1);
			int powerUpToSpawn = ThreadLocalRandom.current().nextInt(0, 3);
			
			switch(powerUpToSpawn)
			{
				case 0:
					speedBoost sb = new speedBoost(gameObjects.textures.get(14), randX, 950, 50, 50, randTicksToLive);
					gameObjects.allSprites.add(sb);
					break;
				case 1:
					fireRateBoost frb = new fireRateBoost(gameObjects.textures.get(15), randX, 950, 50, 50, randTicksToLive);
					gameObjects.allSprites.add(frb);
					break;
				case 2:
					shieldPickup sp = new shieldPickup(gameObjects.textures.get(16), randX, 950, 75, 75, randTicksToLive);
					gameObjects.allSprites.add(sp);            
					break;
			}               
		}
	}
	
	public static void endGame(boolean victory)
	{
		/*
		 * Method: 			endGame
		 * Author: 			Patrick
		 * Description: 	Handles the event of the game ending and is called with a boolean designating victory for the player
		 */
		
		gameObjects.bossHealthBar.setEnabled(false);
		gameObjects.endGameCalled = true;
		System.out.println("called!");
		
		if(victory)
		{
			level.setLevelNum(-1);
			for(projectile p : gameEngine.gameObjects.projectiles)
			{
				gameObjects.spritesToRemove.add(p);
			}
		}
		else
		{
			level.setLevelNum(-2);
		}
	}
	
	public void resetGame()
	{
		/*
		 * Method: 			resetGame
		 * Author: 			Patrick
		 * Description: 	Resets all global variables which are stored in gameObjects
		 */
		
		gameEngine.gameObjects.screenScale = new mathVector(Toolkit.getDefaultToolkit().getScreenSize().width/1920, (Toolkit.getDefaultToolkit().getScreenSize().height/1080));
		gameEngine.gameObjects.allSprites = new CopyOnWriteArrayList<baseSprite>();
		gameEngine.gameObjects.enemies = new CopyOnWriteArrayList<enemy>();
		gameEngine.gameObjects.textures = new CopyOnWriteArrayList<Image>();
		gameEngine.gameObjects.player = new sprites.player(500, 950, 75, 75, 10);
		gameEngine.gameObjects.boss = new sprites.boss(9999, 9999, 100, 100, 1);
		gameEngine.gameObjects.tickSpeed = 50; //1000/tickspeed=framerate
		gameEngine.gameObjects.decensionSpeed = 10;
		gameEngine.gameObjects.enemyChanceToFire = 1; // If random number between 0 and 100 is equal to or below this fire. Acts as a percentage chance
		gameEngine.gameObjects.powerupSpawnChance = 1;
		gameEngine.gameObjects.projectiles = new CopyOnWriteArrayList<projectile>();
		gameEngine.gameObjects.spritesToRemove = new CopyOnWriteArrayList<baseSprite>();
		gameEngine.gameObjects.rightWall = new sprites.wall((int) (1850 * gameEngine.gameObjects.screenScale.x), (int) (0 * gameEngine.gameObjects.screenScale.y), (int) (150 * gameEngine.gameObjects.screenScale.x), (int) (1025 * gameEngine.gameObjects.screenScale.y), direction.right);
		gameEngine.gameObjects.leftWall = new sprites.wall((int) (-50 * gameEngine.gameObjects.screenScale.x), (int) (0 * gameEngine.gameObjects.screenScale.y), (int) (10 * gameEngine.gameObjects.screenScale.x), (int) (1025 * gameEngine.gameObjects.screenScale.y), direction.left);
		gameEngine.gameObjects.bottomWall = new sprites.wall((int) (0 * gameEngine.gameObjects.screenScale.x), (int) (900* gameEngine.gameObjects.screenScale.y), (int) (2000 * gameEngine.gameObjects.screenScale.x), (int) (1* gameEngine.gameObjects.screenScale.y), direction.down);
		gameEngine.gameObjects.maxLevels = 1; 
		gameEngine.gameObjects.bossHealthBar = new healthBar(gameEngine.gameObjects.boss, new mathVector(1000, 50), new mathVector(400, 50));
		gameEngine.gameObjects.fullMovement = false;
		gameEngine.gameObjects.startGameMenuItem = new menuItemStartGame(new mathVector(500, 100), new mathVector(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2 - 250, Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2));
		gameEngine.gameObjects.fullMovementMenuItem = new menuItemMovementDirections(new mathVector(100, 100), new mathVector(50, gameEngine.gameObjects.startGameMenuItem.getPosition().y / gameEngine.gameObjects.screenScale.y));
		gameEngine.gameObjects.projectileLimitMenuItem = new menuItemProjectileLimit(new mathVector(100, 100), new mathVector(200, gameEngine.gameObjects.startGameMenuItem.getPosition().y / gameEngine.gameObjects.screenScale.y));
		gameEngine.gameObjects.projectileLimit = false;
		gameEngine.gameObjects.exclusiveMoveOnInputMenuItem = new menuItemExclusiveMoveOnInput(new mathVector(100, 100), new mathVector(350, gameEngine.gameObjects.startGameMenuItem.getPosition().y / gameEngine.gameObjects.screenScale.y));
		gameEngine.gameObjects.exclusiveMoveOnInput = false;
		gameEngine.gameObjects.levelIncreaseMenuItem = new menuItemLevelIncrease(new mathVector(100, 100), new mathVector(1300, gameEngine.gameObjects.startGameMenuItem.getPosition().y / gameEngine.gameObjects.screenScale.y));
		gameEngine.gameObjects.levelDecreaseMenuItem = new menuItemLevelDecrease(new mathVector(100, 100), new mathVector(1450, gameEngine.gameObjects.startGameMenuItem.getPosition().y / gameEngine.gameObjects.screenScale.y));
		gameEngine.gameObjects.endGameCalled = false;
	}
	

	public gameEngine(drawer d, boolean gotoMenu, int maxLevels) throws IOException
	{
		/*
		 * Method: 			Constructor
		 * Author: 			Patrick
		 * Description: 	Sets up drawing and multi-threads it's inherited run function
		 */
		
		System.out.println("New game engine initialized");
		
		this.drawer = d;
		
		this.loopingMusic = null;
		
		if(gotoMenu)
			level.setLevelNum(-10);
		else
			level.setLevelNum(0);
		
		Thread worker = new Thread(this);
		this.setWorker(worker);
		worker.start();
	}

	public Thread getWorker() {
		return worker;
	}

	public void setWorker(Thread worker) {
		this.worker = worker;
	}
	
	public synchronized static void playSound(String filename, boolean loopForever) 
	{
		try 
		{
			Clip clip = AudioSystem.getClip();
		    AudioInputStream inputStream = AudioSystem.getAudioInputStream(gameEngine.class.getClassLoader().getResource("sound/" + filename + ".wav"));
		    clip.open(inputStream);
		    if(loopForever)
		    {
		    	clip.loop(clip.LOOP_CONTINUOUSLY);
		    	if(gameEngine.loopingMusic != null)
		    		gameEngine.loopingMusic.close();
		    	
		    	gameEngine.loopingMusic = clip;
		    }  	
		    
		    clip.start(); 
		    
		} 
		catch (Exception e) 
		{ 
			e.printStackTrace();
		}
		
	}
	
	public void collisionHandler(baseSprite s, baseSprite bs)
	{
		/*
		 * Method: 			collisionHandler
		 * Author: 			Patrick
		 * Description: 	Called after checking for a collision
		 */
		
		if (s.equals(bs)) // Don't check for collisions with self
			return;

		if (collisionChecker.checkCollision(s, bs)) {
			if (!s.isHitWall() && !bs.isHitWall()) // If the sprite has hit a side wall don't delete it
			{
				s.setHealth(s.getHealth() - 1.0f);
				bs.setHealth(bs.getHealth() - 1.0f);
				if (s.getHealth() <= 0)
					gameObjects.spritesToRemove.add(s);
				if (bs.getHealth() <= 0)
					gameObjects.spritesToRemove.add(bs);
			} 
		}
	}
	
	public void enemyFriendlyFireChecker(enemy e)
	{
		/*
		 * Method: 			enemyFriendlyFireChecker
		 * Author: 			Patrick
		 * Description: 	This method checks for other enemies colliding with e's friendChecker boundingBox, this prevents 'friendly fire'
		 */
		
		e.setCanFire(true);
		for(enemy en : gameObjects.enemies)
		{
			if(e.equals(en)) // Don't check for collisions with self
				continue;
			
			if(collisionChecker.checkCollision(e.getFriendlyChecker(), en))
			{
				e.setCanFire(false); // If another enemy is blocking it's shot downwards, don't shoot
			}
		}
	}
	
	public void setup()
	{
		/*
		 * Method: 			setup
		 * Author: 			Patrick
		 * Description: 	This method sets initialises non global variables and adds them to global lists thereby setting them up for the game
		 */
		
		// Load variables from last instance
		gameEngine.gameObjects.maxLevels = this.drawer.maxLevelStore;
		level.setLevelNum(this.drawer.nextLevelStore);
				
		try {this.gameObjects.textures = this.loadTextures();} catch (IOException e2) {e2.printStackTrace();} //	Load textures
				
		//	Load font
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("fonts/VT323-Regular.ttf");
		try {gameObjects.spaceFont = Font.createFont(Font.TRUETYPE_FONT, is);} 
		catch (FontFormatException e1) {e1.printStackTrace();} 
		catch (IOException e1) {e1.printStackTrace();}
				
		gameObjects.allSprites.add(gameObjects.leftWall);
		gameObjects.allSprites.add(gameObjects.rightWall);
		gameObjects.allSprites.add(gameObjects.bottomWall);
				
				
		// Add player to allSprites and now set loaded texture
		gameObjects.allSprites.add(gameObjects.player);
		gameObjects.player.spriteImage = gameObjects.textures.get(0);
				
		// Add boss to allSprites and now set loaded texture
		gameObjects.allSprites.add(gameObjects.boss);
		gameObjects.boss.spriteImage = gameObjects.textures.get(2);
				
		if(level.getLevelNum() < 0)
		{
			//Setup Menu Item textures
			gameObjects.allSprites.add(gameObjects.fullMovementMenuItem);
			gameObjects.fullMovementMenuItem.spriteImage = gameEngine.gameObjects.textures.get(18); // unchecked box
					
			gameObjects.allSprites.add(gameObjects.exclusiveMoveOnInputMenuItem);
			gameObjects.exclusiveMoveOnInputMenuItem.spriteImage = gameEngine.gameObjects.textures.get(18); // unchecked box
					
			gameObjects.allSprites.add(gameObjects.startGameMenuItem);
			gameObjects.startGameMenuItem.spriteImage = gameEngine.gameObjects.textures.get(18); // unchecked box
			
			gameObjects.allSprites.add(gameObjects.projectileLimitMenuItem);
			gameObjects.projectileLimitMenuItem.spriteImage = gameEngine.gameObjects.textures.get(18); // unchecked box
					
			gameObjects.allSprites.add(gameObjects.levelIncreaseMenuItem);
			gameObjects.levelIncreaseMenuItem.spriteImage = gameEngine.gameObjects.textures.get(19); // up arrow
					
			gameObjects.allSprites.add(gameObjects.levelDecreaseMenuItem);
			gameObjects.levelDecreaseMenuItem.spriteImage = gameEngine.gameObjects.textures.get(20); // down arrow
		}
				
		// Calculate initial boundingBox values to prevent methods being called with null values
		for(baseSprite s : gameObjects.allSprites)
		{
			s.calcBoundingBox();
		}
	}

	@Override
	public void run()
	{
		/*
		 * Method: 			run
		 * Author: 			Patrick
		 * Description: 	This is the main game function and contains the game loop. The beginning of the function creates all necessary objects
		 * 					before the main loop is run continuously until the game ends, the gameEngine instance is destroyed and the thread interrupted
		 */
		
		System.out.println("Beginning New Game!");
		
		//	Initialise all global variables
		resetGame();
		//	Setup the game
		setup();
		
		long lastTick = System.nanoTime();
		
		//playSound("main", true);
		
		
		
		while(this.destroy == false) // Main game loop
		{

			drawer.repaint(); // redraw the display
			
			if(level.getLevelNum() >= 0)
				spawnPickups(); // possibly spawn powerups for the player
			
			for(baseSprite s : gameObjects.allSprites)
				s.tick(); // call tick on every sprite
			
			for(baseSprite s : gameObjects.allSprites)
				for(baseSprite bs : gameObjects.allSprites)
					collisionHandler(s, bs);
			
			removeSprites(gameObjects.spritesToRemove); // remove any sprites that have collided
			
			for(enemy e : gameObjects.enemies)
				enemyFriendlyFireChecker(e);
			
			
			if(!gameObjects.endGameCalled && level.getLevelNum() >= 0)
			{
				if(level.checkForWinner() == 0 && level.getLevelNum() < gameObjects.maxLevels || level.getLevelNum() == 0)
					level.newLevel(level.getLevelNum()*15 + 15);
				else if(level.checkForWinner() == 1)
					endGame(true);
				else if(level.checkForWinner() == 2)
					endGame(false);
			}
						
		
			/* Ensure a consistent tick time and save CPU time */
			long thisTick = System.nanoTime();
			long currentTickTime = (thisTick - lastTick)/1000000;
			long timeToNextTick = gameObjects.tickSpeed - currentTickTime;
			
			
			if(timeToNextTick>0)
			{
				try
				{
					Thread.sleep(timeToNextTick);
				} 
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				
			}
			
			lastTick = System.nanoTime();
		}
		
		/* On shutdown of gameEngine */
		
		if(this.loopingMusic != null)
			this.loopingMusic.close();
		this.getWorker().interrupt();
		System.out.println("Thread " + this.getWorker().getId() + " is " + ((this.getWorker().isInterrupted()) ? "" : "not") + "interrupted" );
		return;
		
	}

}
