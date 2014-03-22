package pong;

import java.awt.*;

public class Sprite 
{
	private Image img;
	private float x;
	private float y;
	private float vx;
	private float vy;
	   
	//constructor
	public Sprite(Image img)
	{
	   this.img = img;
	}
	
	//change position
	public void update()
	{
		x += vx;
		y += vy;
	}
	
	//get sprite/image
	public Image getImage()
	{
		return img;
	}
	
	//set vertical velocity
	public void setVelocityY(float vy)
	{
		this.vy = vy;
	}
	
	//set horizontal velocity
	public void setVelocityX(float vx)
	{
		this.vx = vx;
	}
	
	//get vertical velocity
	public float getVelocityY()
	{
		return vy;
	}
	
	//get horizontal velocity
	public float getVelocityX()
	{
		return vx;
	}
	
	//get sprite height
	public int getHeight()
	{
		return img.getHeight(null);
	}
	
	//get sprite width
	public int getWidth()
	{
		return img.getWidth(null);
	}
	
	//set sprite y position
    public void setY(float y)
	{
		this.y = y;
	}
	
	//set sprite x position
	public void setX(float x)
	{
		this.x = x;
	}
	
	//get x position
	public float getX()
	{
		return x;
	}
	//get y position
	public float getY()
	{
		return y;
	}
}
