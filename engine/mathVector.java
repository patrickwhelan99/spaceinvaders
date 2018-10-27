package engine;

public class mathVector 
{
	/*
	 * Class: 			mathVector
	 * Author: 			Patrick
	 * Description: 	Basic class representing a vector, with basic mathematical functions
	 */
	
	public double x;
	public double y;
	
	public mathVector()
	{
		this.x = 0;
		this.y = 0;
	}
	
	public mathVector(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public mathVector multiply(mathVector b)
	{
		return new mathVector(this.x*b.x, this.y*b.y);
	}
	
	public double magnitude()
	{
		return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
	}
	
	public double dotProduct(mathVector vec2)
	{
		return Math.sqrt(Math.pow((this.x*vec2.x), 2) + Math.pow(this.y*vec2.y, 2));
	}
	
	public mathVector unitVector()
	{
		double mag = this.magnitude();
		return new mathVector(this.x/mag, this.y/mag);
	}
}
