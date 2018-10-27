package sprites;

import engine.mathVector;

public class healthBar 
{
	/*
	 * Class: 			healthBar 
	 * Author: 			Patrick
	 */
	
	private baseSprite spritePointer; 
	private float originalHealth; 
	private mathVector originalDrawingSize;
	private mathVector drawingSize;
	private mathVector drawingPosition;
	private boolean enabled = false;
	
	public healthBar(baseSprite spritePointer, mathVector drawingSize, mathVector drawingPosition)
	{
		this.spritePointer = spritePointer;
		this.originalHealth = spritePointer.getHealth();
		this.setOriginalDrawingSize(drawingSize);
		this.setDrawingSize(drawingSize);
		this.setDrawingPosition(drawingPosition);
	}
	
	public mathVector calcDrawingSize() // calculate the length of the health bar
	{
		return new mathVector(((this.spritePointer.getHealth() / this.originalHealth)*100)*10, this.getOriginalDrawingSize().y);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public mathVector getDrawingSize() {
		return drawingSize;
	}

	public void setDrawingSize(mathVector drawingSize) {
		this.drawingSize = drawingSize;
	}

	public mathVector getDrawingPosition() {
		return drawingPosition;
	}

	public void setDrawingPosition(mathVector drawingPosition) {
		this.drawingPosition = drawingPosition;
	}

	public mathVector getOriginalDrawingSize() {
		return originalDrawingSize;
	}

	public void setOriginalDrawingSize(mathVector originalDrawingSize) {
		this.originalDrawingSize = originalDrawingSize;
	}
}
