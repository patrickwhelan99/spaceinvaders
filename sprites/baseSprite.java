package sprites;

import java.awt.Component;
import java.awt.Image;
import java.util.ArrayList;
import sprites.projectile;
import engine.direction;
import engine.gameEngine;
import engine.gameObjects;
import engine.mathVector;


@SuppressWarnings("serial")
public class baseSprite extends Component
{
	/*
	 * Class: 			baseSprite 
	 * Author: 			Patrick
	 */
	
	private mathVector position;
	private boolean isDestroyed = false;
	private float health = 1.0f;
	public Image spriteImage;
	private direction currentDirection = direction.right;
	private mathVector centre;
	protected ArrayList<mathVector> boundingBox = new ArrayList<mathVector>(); 
	private mathVector drawingSize;
	private boolean hitWall;
	private int movementDistance = 75;
	protected int ticksSinceMove = 0;
	protected int ticksSinceFire = 0;
	protected int projectileFireSpeed = 25;
	private int tickMinFire = 24;
	private mathVector projectileSpawnPosition;
	private boolean scaled = false;
	
	public void move(direction d, int movementDistance)
	{
		/*
		 * Method: 			move
		 * Author: 			Patrick
		 * Description: 	This method is called every tick for each sprite and handles sprite movement
		 */
		
		switch(d)
		{
			case up:
				this.setPosition(new mathVector(this.getPosition().x, this.getPosition().y - movementDistance));
				break;
			case right:
				this.setPosition(new mathVector(this.getPosition().x + movementDistance, this.getPosition().y));
				break;
			case down:
				this.setPosition(new mathVector(this.getPosition().x, this.getPosition().y + movementDistance));
				break;
			case left:
				this.setPosition(new mathVector(this.getPosition().x - movementDistance, this.getPosition().y));
				break;
			default:
				System.err.println("Invalid direction passed to move()");
				break;	
		}
	}
	
	synchronized public void fireProjectile(engine.direction directionToFire)
	{
		/*
		 * Method: 			fireProjectile
		 * Author: 			Patrick
		 * Description: 	This method is called by both enemies and the player class to fire projectiles
		 */
		
		if(this.getTicksSinceFire() > this.getTickMinFire() || (gameEngine.gameObjects.projectileLimit && gameEngine.gameObjects.projectiles.size() > 40))
			return;
		
		this.setProjectileSpawnPosition(this.getProjectileSpawnPosition());
		
		projectile newProjectile = new projectile(gameEngine.gameObjects.textures.get(1), this.getProjectileSpawnPosition(), directionToFire, 10, 25, this, this.projectileFireSpeed);
		newProjectile.setScaled(true);
		newProjectile.calcBoundingBox();
		gameEngine.gameObjects.allSprites.add(newProjectile);
		gameEngine.gameObjects.projectiles.add(newProjectile);
		this.setTicksSinceFire(0);
		
		gameEngine.playSound("shot", false);
	}
	
	public void calcCentre()
	{
		this.setCentre(new mathVector(this.getPosition().x + (this.getDrawingSize().x/2), this.getPosition().y + (this.getDrawingSize().y/2)));
	}
	
	public void calcBoundingBox()
	{
		/*
		 * Method: 			calcBoundingBox
		 * Author: 			Patrick
		 * Description: 	Called every tick after moving the sprite, this method calculates the sprite's bounding box which is
		 * 					used to calculated collisions between sprites
		 */
		this.boundingBox.clear();
		this.calcCentre();
		this.boundingBox.add(getCentre());
		this.boundingBox.add(new mathVector(this.getPosition().x, this.getPosition().y)); // Upper left vertex
		this.boundingBox.add(new mathVector(this.getPosition().x + this.getDrawingSize().x, this.getPosition().y)); // Upper right vertex
		this.boundingBox.add(new mathVector(this.getPosition().x, this.getPosition().y + this.getDrawingSize().y)); // Bottom left vertex
		this.boundingBox.add(new mathVector(this.getPosition().x + this.getDrawingSize().x, this.getPosition().y + this.getDrawingSize().y)); // Bottom right vertex
	}

	public void tick() // 
	{
		/*
		 * Method: 			tick
		 * Author: 			Patrick
		 * Description: 	Method called for sprite every tick (loop)
		 */
		this.setTicksSinceFire(this.getTicksSinceFire() + 1);
		this.setHitWall(false);
		this.move(this.currentDirection, this.getMovementDistance());
		this.calcBoundingBox();
		this.setProjectileSpawnPosition(new mathVector(this.getPosition().x, (this.getPosition().y + this.getDrawingSize().y/2 + 10)));
	}
	
	public baseSprite(Image texture) 
	{
		this.spriteImage = texture;
		this.setPosition(new mathVector(0, 0));
	}
	
	public baseSprite(Image texture, int startingX, int startingY, int drawingX, int drawingY, int movementDistance) 
	{
		this.spriteImage = texture;
		this.setPosition(new mathVector(startingX, startingY));
		this.setDrawingSize(new mathVector(drawingX, drawingY));
		this.setMovementDistance(movementDistance);
	}
	
	public baseSprite(int startingX, int startingY, int drawingX, int drawingY, int movementDistance) 
	{
		this.setPosition(new mathVector(startingX, startingY));
		this.setDrawingSize(new mathVector(drawingX, drawingY));
		this.setMovementDistance(movementDistance);
	}
	
	public baseSprite(int startingX, int startingY) 
	{
		this.setPosition(new mathVector(startingX, startingY));
	}
	
	public baseSprite(int startingX, int startingY, int drawingX, int drawingY) 
	{
		this.setPosition(new mathVector(startingX, startingY));
		this.setDrawingSize(new mathVector(drawingX, drawingY));
	}


	public direction getCurrentDirection() {
		return currentDirection;
	}



	public void setCurrentDirection(direction currentDirection) {
		this.currentDirection = currentDirection;
	}



	public float getHealth() {
		return health;
	}



	public void setHealth(float health) {
		this.health = health;
	}



	public boolean isDestroyed() {
		return isDestroyed;
	}



	public void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}


	public mathVector getPosition() {
		return position;
	}
	
	public void setPosition(mathVector position) {
		this.position = position;
	}


	public mathVector getCentre() {
		this.calcCentre();
		return centre;
	}


	void setCentre(mathVector centre) {
		this.centre = centre;
	}


	public ArrayList<mathVector> getBoundingBox() {
		return this.boundingBox;
	}


	private void setBoundingBox(ArrayList<mathVector> boundingBox) {
		this.boundingBox = boundingBox;
	}


	public mathVector getDrawingSize() {
		return drawingSize;
	}


	public void setDrawingSize(mathVector drawingSize) {
		this.drawingSize = drawingSize;
	}


	public boolean isHitWall() {
		return hitWall;
	}


	public void setHitWall(boolean hitWall) {
		this.hitWall = hitWall;
	}


	public int getMovementDistance() {
		return movementDistance;
	}


	public void setMovementDistance(int movementDistance) {
		this.movementDistance = movementDistance;
	}


	public int getTicksSinceMove() {
		return ticksSinceMove;
	}


	public void setTicksSinceMove(int ticksSinceMove) {
		this.ticksSinceMove = ticksSinceMove;
	}

	public int getTicksSinceFire()
	{
		return ticksSinceFire;
	}

	public void setTicksSinceFire(int ticksSinceFire)
	{
		this.ticksSinceFire = ticksSinceFire;
	}

	public mathVector getProjectileSpawnPosition()
	{
		return projectileSpawnPosition;
	}

	public void setProjectileSpawnPosition(mathVector projectileSpawnPosition)
	{
		this.projectileSpawnPosition = projectileSpawnPosition;
	}

	public int getTickMinFire()
	{
		return tickMinFire;
	}

	public void setTickMinFire(int tickMinFire)
	{
		this.tickMinFire = tickMinFire;
	}

	public boolean isScaled() {
		return scaled;
	}

	public void setScaled(boolean scaled) {
		this.scaled = scaled;
	}
}




