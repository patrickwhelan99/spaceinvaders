package graphics;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import engine.direction;
import engine.gameEngine;
import engine.gameObjects;
import engine.level;
import engine.mathVector;
import sprites.baseSprite;
import sprites.enemy;
import sprites.healthBar;
import sprites.player;
import sprites.projectile;
import sprites.wall;
import sprites.menu.menuItemStartGame;
import sprites.pickup.pickup;
import sprites.pickup.shield;



@SuppressWarnings("serial")

public class drawer extends JPanel implements KeyListener
{
	/*
	 * Class: 			drawer 
	 * Author: 			Patrick
	 * Description:		The main class which initialises the gameEngine and draws to screen
	 */
	
	public gameEngine engine;
	public int maxLevelStore = 3; // allows the var to be stored between the destruction and creation of gameEngines
	public int nextLevelStore = -10;
	
	@Override
	public void paintComponent(Graphics g) 
	{		
		/*
		 * Method: 			paintComponent
		 * Author: 			Patrick
		 * Description: 	Draws to screen
		 */
		
		super.paintComponent(g);
		
		g.setColor(Color.red);
		
		
		g.drawImage(gameEngine.gameObjects.textures.get(13), 0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, null); // background
		
		for(baseSprite bs : gameEngine.gameObjects.allSprites)
		{
			
			//bs.setDrawingSize(new mathVector(bs.getDrawingSize().x * (1920/Toolkit.getDefaultToolkit().getScreenSize().width), bs.getDrawingSize().y * (1080/Toolkit.getDefaultToolkit().getScreenSize().height)));
			if(bs instanceof wall || bs instanceof menuItemStartGame)
				bs.setScaled(true);
			
			if(!bs.isScaled())
			{
				bs.setPosition(new mathVector(bs.getPosition().x * (Toolkit.getDefaultToolkit().getScreenSize().width/1920), bs.getPosition().y * (Toolkit.getDefaultToolkit().getScreenSize().height/1080)));
				bs.setScaled(true);
			}
			
			//System.out.println("{" + bs.getPosition().x + ", " + bs.getPosition().y + "}\n");
			
			if(bs instanceof pickup)
			{
				pickup pu = (pickup)bs;
				if(pu.isPickedUp() || !pu.ShouldDraw())
					continue; // if pickup has already been collected don't draw
			}
			
			if(bs instanceof shield)
			{
				g.drawImage(bs.spriteImage, (int)bs.getBoundingBox().get(1).x, (int)bs.getBoundingBox().get(1).y, (int)bs.getDrawingSize().x, (int)bs.getDrawingSize().y, null);
				continue;
			}
			
			
			g.drawImage(bs.spriteImage, (int)bs.getPosition().x, (int)bs.getPosition().y, (int)bs.getDrawingSize().x, (int)bs.getDrawingSize().y, null);
		}
		
		
		if(gameEngine.gameObjects.bossHealthBar.isEnabled())
		{
			gameEngine.gameObjects.bossHealthBar.setDrawingSize((gameEngine.gameObjects.bossHealthBar.calcDrawingSize()));
			g.drawRect((int)(gameEngine.gameObjects.bossHealthBar.getDrawingPosition().x*gameEngine.gameObjects.screenScale.x), (int)(gameEngine.gameObjects.bossHealthBar.getDrawingPosition().y*gameEngine.gameObjects.screenScale.y), (int)gameEngine.gameObjects.bossHealthBar.getOriginalDrawingSize().x, (int)gameEngine.gameObjects.bossHealthBar.getOriginalDrawingSize().y);
			g.fillRect((int)(gameEngine.gameObjects.bossHealthBar.getDrawingPosition().x*gameEngine.gameObjects.screenScale.x), (int)(gameEngine.gameObjects.bossHealthBar.getDrawingPosition().y*gameEngine.gameObjects.screenScale.y), (int)gameEngine.gameObjects.bossHealthBar.getDrawingSize().x, (int)gameEngine.gameObjects.bossHealthBar.getDrawingSize().y);
		}
			
		if(level.getLevelNum() == -1) // Level num -1 = player win
		{
			g.setColor(Color.WHITE);
			gameEngine.gameObjects.spaceFont = gameEngine.gameObjects.spaceFont.deriveFont(0, 156);
			g.setFont(gameEngine.gameObjects.spaceFont);
			
			FontMetrics fm = getFontMetrics( getFont() );
			int textWidth = fm.stringWidth("The Planet is saved!");
			
			
			g.drawString("The Planet is saved!", (int) (Toolkit.getDefaultToolkit().getScreenSize().width/2 - (textWidth*gameEngine.gameObjects.screenScale.x)*gameEngine.gameObjects.screenScale.x), Toolkit.getDefaultToolkit().getScreenSize().height/2);
		}
		
		if(level.getLevelNum() == -2) // Level num -2 = player loss
		{
			g.setColor(Color.WHITE);
			gameEngine.gameObjects.spaceFont = gameEngine.gameObjects.spaceFont.deriveFont(0, 156);
			g.setFont(gameEngine.gameObjects.spaceFont);
			FontMetrics fm = getFontMetrics( getFont() );
			int textWidth = fm.stringWidth("Game Over!");
			g.drawString("Game Over!", (int) (Toolkit.getDefaultToolkit().getScreenSize().width/2 - (textWidth*gameEngine.gameObjects.screenScale.x)*gameEngine.gameObjects.screenScale.x), Toolkit.getDefaultToolkit().getScreenSize().height/2);
			gameEngine.gameObjects.spaceFont = gameEngine.gameObjects.spaceFont.deriveFont(0, 72);
			g.setFont(gameEngine.gameObjects.spaceFont);
			fm = getFontMetrics( getFont() );
			textWidth = fm.stringWidth("Press enter to start a new game");
			g.drawString("Press enter to start a new game", (int) (Toolkit.getDefaultToolkit().getScreenSize().width/2 - (textWidth*gameEngine.gameObjects.screenScale.x)), (int)(Toolkit.getDefaultToolkit().getScreenSize().height/2 + 100 * gameEngine.gameObjects.screenScale.y));
		}
		
		if(level.getLevelNum() == -10) // Level num -2 = player loss
		{
			g.setColor(Color.WHITE);
			gameEngine.gameObjects.spaceFont = gameEngine.gameObjects.spaceFont.deriveFont(0, 156);
			g.setFont(gameEngine.gameObjects.spaceFont);
			FontMetrics fm = getFontMetrics( getFont() );
			int textWidth = fm.stringWidth("Main Menu");
			g.drawString("Main Menu", (int)(Toolkit.getDefaultToolkit().getScreenSize().width/2 - (textWidth*gameEngine.gameObjects.screenScale.x)*gameEngine.gameObjects.screenScale.x), (int)(200*gameEngine.gameObjects.screenScale.y));
			
			gameEngine.gameObjects.spaceFont = gameEngine.gameObjects.spaceFont.deriveFont(0, 24);
			g.setFont(gameEngine.gameObjects.spaceFont);
			
			
			fm = getFontMetrics( getFont() );
			textWidth = fm.stringWidth("Full movement");
			g.drawString("Full movement", (int)(gameEngine.gameObjects.fullMovementMenuItem.getPosition().x + gameEngine.gameObjects.fullMovementMenuItem.getDrawingSize().x/2 - textWidth/2 * gameEngine.gameObjects.screenScale.x), (int)(gameEngine.gameObjects.fullMovementMenuItem.getPosition().y - 25 * gameEngine.gameObjects.screenScale.y));
			
			
			textWidth = fm.stringWidth("Limited Projectiles");
			g.drawString("Limited Projectiles", (int)(gameEngine.gameObjects.projectileLimitMenuItem.getPosition().x + gameEngine.gameObjects.projectileLimitMenuItem.getDrawingSize().x/2 - textWidth/2 * gameEngine.gameObjects.screenScale.x), (int)(gameEngine.gameObjects.projectileLimitMenuItem.getPosition().y + gameEngine.gameObjects.projectileLimitMenuItem.getDrawingSize().y + 25 * gameEngine.gameObjects.screenScale.y));
			
			
			textWidth = fm.stringWidth("Move on input");
			g.drawString("Move on input", (int)(gameEngine.gameObjects.exclusiveMoveOnInputMenuItem.getPosition().x + gameEngine.gameObjects.exclusiveMoveOnInputMenuItem.getDrawingSize().x/2 - textWidth/2 * gameEngine.gameObjects.screenScale.x), (int)(gameEngine.gameObjects.exclusiveMoveOnInputMenuItem.getPosition().y - 25 * gameEngine.gameObjects.screenScale.y));
			
			
			
			g.setColor(Color.GREEN);
			String infoText = "Instructions:\n\nUse the arrow keys to move and the space bar to fire.\nHit the boxes to interact with them.\nSelect a number of levels to play.\n\nThe last level will always be a boss battle!\n\nPress escape at any time to return to the main menu";
			int y = (int) (200*gameEngine.gameObjects.screenScale.y);
			
			for (String l : infoText.split("\n")) // Draw string cannot handle new lines
		        g.drawString(l, (int)(1300*gameEngine.gameObjects.screenScale.x), y += g.getFontMetrics().getHeight()); // offset each line by font's lineHeight
			
			
			
			
			
			g.setColor(Color.WHITE);
			gameEngine.gameObjects.spaceFont = gameEngine.gameObjects.spaceFont.deriveFont(0, 56);
			g.setFont(gameEngine.gameObjects.spaceFont);
			
			g.drawString("Start Game", (int)(gameEngine.gameObjects.startGameMenuItem.getPosition().x + gameEngine.gameObjects.startGameMenuItem.getDrawingSize().x/2 - textWidth/2 * gameEngine.gameObjects.screenScale.x), (int)(gameEngine.gameObjects.startGameMenuItem.getPosition().y - 25 * gameEngine.gameObjects.screenScale.y));
			
			
			fm = getFontMetrics( getFont() );
			textWidth = fm.stringWidth("Levels");
			g.drawString("Levels", (int) (1650*gameEngine.gameObjects.screenScale.x - textWidth/2 * gameEngine.gameObjects.screenScale.x), (int) (500*gameEngine.gameObjects.screenScale.y));
			
			textWidth = fm.stringWidth(Integer.toString(gameEngine.gameObjects.maxLevels));
			g.drawString(Integer.toString(gameEngine.gameObjects.maxLevels), (int) (1650*gameEngine.gameObjects.screenScale.x - textWidth/2 * gameEngine.gameObjects.screenScale.x), (int) (600*gameEngine.gameObjects.screenScale.y));
			
		}
		
	}
	
	@Override
	public Dimension getPreferredSize() 
	{
		return new Dimension(1920, 1080);
	}
	
	public void addPaneltoFrame(Container container) 
	{
		container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
		container.add(this);
	}
	
	 public void addNotify() 
	 {
		 super.addNotify();
		 requestFocus();
	 }
	
	@SuppressWarnings("unused")
	public drawer()
	{
		super();
		try
		{
			gameEngine engine = new gameEngine(this, true, 0); // Initialise the game engine
			this.engine = engine;
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		addKeyListener(this);
	
		this.setBounds(0, 0, 1920, 1080);
		this.setSize(1920, 1080);
		
	}
	

	

	@Override
	public void keyPressed(KeyEvent key)
	{	
		if(key.getKeyCode() == KeyEvent.VK_RIGHT)
			gameEngine.gameObjects.player.setCurrentDirection(direction.right);
		if(key.getKeyCode() == KeyEvent.VK_LEFT)
			gameEngine.gameObjects.player.setCurrentDirection(direction.left);
		if(key.getKeyCode() == KeyEvent.VK_UP && gameEngine.gameObjects.fullMovement && !gameEngine.gameObjects.exclusiveMoveOnInput)
			gameEngine.gameObjects.player.setCurrentDirection(direction.up);
		if(key.getKeyCode() == KeyEvent.VK_DOWN && gameEngine.gameObjects.fullMovement && !gameEngine.gameObjects.exclusiveMoveOnInput)
			gameEngine.gameObjects.player.setCurrentDirection(direction.down);
		
		if(gameEngine.gameObjects.exclusiveMoveOnInput) // To comply with requirement of being able to move in all 8 directions
		{
			if(key.getKeyCode() == KeyEvent.VK_RIGHT)
			{
				player.move(direction.right);
			}
			if(key.getKeyCode() == KeyEvent.VK_LEFT)
			{
				player.move(direction.left);
			}
			if(gameEngine.gameObjects.fullMovement)
			{
				if(key.getKeyCode() == KeyEvent.VK_UP)
				{
					player.move(direction.up);
				}
				if(key.getKeyCode() == KeyEvent.VK_DOWN)
				{
					player.move(direction.down);
				}
			}
		}
		
		
		if(key.getKeyCode() == KeyEvent.VK_SPACE)
		{
			if(gameEngine.gameObjects.player.getTicksSinceFire() > gameEngine.gameObjects.player.getTickMinFire())
			{
				gameEngine.gameObjects.player.setProjectileSpawnPosition(new mathVector(gameEngine.gameObjects.player.getPosition().x, (gameEngine.gameObjects.player.getPosition().y - gameEngine.gameObjects.player.getDrawingSize().y/2 - 10)));
				gameEngine.gameObjects.player.fireProjectile(gameEngine.gameObjects.player.getCurrentDirection());
				gameEngine.gameObjects.player.setTicksSinceFire(0);
				gameEngine.gameObjects.player.fireProjectile(direction.up);
			}
		}
		
		if(key.getKeyCode() == KeyEvent.VK_ENTER && level.getLevelNum() < 0)
		{
			this.engine.destroy = true;
			this.maxLevelStore = this.engine.gameObjects.maxLevels;
			this.nextLevelStore  = 0;
			try {
				this.engine.getWorker().join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Thread is alive? " + this.engine.getWorker().isAlive());
			this.engine = null;
			
		}
		
		if(key.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			this.engine.destroy = true;
			this.maxLevelStore = this.engine.gameObjects.maxLevels;
			this.nextLevelStore  = -10;
			try {
				this.engine.getWorker().join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Thread is " + ((this.engine.getWorker().isAlive()) ? "alive" : "dead" ));
			this.engine = null;
			
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
