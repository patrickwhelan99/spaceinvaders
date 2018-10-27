package sprites.menu;

import engine.mathVector;
import sprites.baseSprite;

public class menuItem extends baseSprite
{	
	/*
	 * Class: 			menuItem 
	 * Author: 			Patrick
	 */
	
	menuItem(mathVector drawingSize, mathVector drawingPosition)
	{
		super((int)drawingPosition.x, (int)drawingPosition.y, (int)drawingSize.x, (int)drawingSize.y, 0);
		this.setDrawingSize(drawingSize);
		this.setPosition(drawingPosition);
	}
	
	public void onHit() {};
}
