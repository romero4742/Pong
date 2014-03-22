package pong;

import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.Robot;
import java.awt.event.KeyEvent;

class Computer 
{
   private Robot robot;
   private float screenWidth,screenHeight,calculatedPosition,rightPosition;

   public Computer(GraphicsDevice vc) 
   {
	  try 
	  {
		robot = new Robot(vc);
	  } catch (AWTException e) 
	  {
		e.printStackTrace();
	  }
   }
   
   public void setRightPosition(float rightPosition)
   {
	   this.rightPosition = rightPosition;
   }
   
   public void calculatePosition(float firstX, float firstY, float secondX, float secondY)
   {
	   float slope = (secondY - firstY)/(secondX - firstX);
	   float b = secondY - (slope*secondX);
	   calculatedPosition = slope*rightPosition + b;
	   if(calculatedPosition > screenHeight)
	   {
		   calculatedPosition = screenHeight - (calculatedPosition - screenHeight);
	   }
	   if(calculatedPosition < 0)
	   {
		   calculatedPosition = -1*calculatedPosition;
	   }
   }
   
   public void setScreenWidth(float screenWidth,float screenHeight)
   {
	   this.screenWidth = screenWidth;
	   this.screenHeight =screenHeight;
   }
   
   /**
    * takes in the current position of the bar and moves it towards the calculated position
    * @param barPosition
    */
   protected void computerMove(float barPosition)
   {
	   try
	   {
		   if(barPosition >= calculatedPosition-5 && barPosition <= calculatedPosition+5)
		   {
			   releaseKeys();
			   return;
		   }
	      if(barPosition < calculatedPosition) // if bar is on top of calculated position move down
	      {
		     robot.keyPress(KeyEvent.VK_DOWN);
		     
	      }
	      else
	      {
		     robot.keyPress(KeyEvent.VK_UP);
	      }
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace();
	   }
   }
   
   protected void releaseKeys()
   {
	   robot.keyRelease(KeyEvent.VK_DOWN);
	   robot.keyRelease(KeyEvent.VK_UP);
   }
}
