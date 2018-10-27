package sprites;

import engine.direction;

@SuppressWarnings("serial")
public class wall extends baseSprite
{
	/*
	 * Class: 			wall 
	 * Author: 			Patrick
	 */
	
	private direction positionalDirection;
	
	public wall(int startingX, int startingY, int drawingX, int drawingY, direction positionalDirection)
	{
		super(startingX, startingY, drawingX, drawingY);
		this.setPositionalDirection(positionalDirection);
		this.calcBoundingBox();
	}
	
	@Override
	public void tick()
	{
		
	}

	public direction getPositionalDirection() {
		return this.positionalDirection;
	}

	private void setPositionalDirection(direction positionalDirection) {
		this.positionalDirection = positionalDirection;
	}
}
