package pong;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;


public class ScreenManager 
{
   private GraphicsDevice vc;
   
   //give vc access to monitor
   public ScreenManager()
   {
	   GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
	   vc = e.getDefaultScreenDevice();
	   
   }
   
   protected GraphicsDevice getGraphicsDevice()
   {
	   return vc;
   }
   
   //Display modes are built in the graphics card so we need to get all dm of g card
   // and match to the one we have
   public DisplayMode[] getCompatibleDisplayModes()
   {
	   return vc.getDisplayModes();
   }
   
   //compares dm passed in and checks if it matches
   public DisplayMode findFirstCompatibleMode(DisplayMode modes[])
   {
	  DisplayMode goodModes[] = vc.getDisplayModes();
	  for(int x = 0; x<modes.length;x++)
	  {
		  for(int y = 0; y < goodModes.length; y++)
		  {
			  if(displayModesMatch(modes[x],goodModes[y]))
			  {
				  return modes[x];
			  }
		  }
	  }
	  return null;
   }
   
   //check if two dm are equal
   public boolean displayModesMatch(DisplayMode dm1, DisplayMode dm2)
   {
	   if(dm1.getWidth() != dm2.getWidth() || dm1.getHeight() != dm2.getHeight())
		   return false;
	   if(dm1.getBitDepth() !=  DisplayMode.BIT_DEPTH_MULTI && dm2.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI && dm1.getBitDepth() != dm2.getBitDepth() )
		   return false;
	   if(dm1.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN && dm2.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN && dm1.getRefreshRate() != dm2.getRefreshRate())
		   return false;
	   return true;
   }
   
   //get current disp mode
   public DisplayMode getCurrentDisplayMode()
   {
	   return vc.getDisplayMode();
   }
   
   public void setFullScreen(DisplayMode dm)
   {
	   JFrame f = new JFrame();
	   f.setIgnoreRepaint(true);
	   f.setUndecorated(true);
	   f.setResizable(false);
	   vc.setFullScreenWindow(f);
	   
	   if(dm != null && vc.isDisplayChangeSupported())
	   {
		   try
		   {
			   vc.setDisplayMode(dm);
		   }
		   catch(Exception e)
		   {}
	   }
	   f.createBufferStrategy(2);
   }
   
   //we will set graphics object = to this
   public Graphics2D getGraphics()
   {
	   Window w = vc.getFullScreenWindow();
	   if(w != null)
	   {
		   BufferStrategy s = w.getBufferStrategy();
		   return (Graphics2D)s.getDrawGraphics();
	   }
	   else
		   return null;
   }
   
   //update display
   public void update()
   {
	   Window w = vc.getFullScreenWindow();
	   if(w!= null)
	   {
		   BufferStrategy s = w.getBufferStrategy();
		   if(!s.contentsLost())
		   {
			   s.show();
			   
		   }
	   }
   }
   
   //return full screen window
   public Window getFullScreenWindow()
   {
	   return vc.getFullScreenWindow();
   }
   
   public int getHeight()
   {
	   Window w = vc.getFullScreenWindow();
	   if(w != null)
		   return w.getHeight();
	   else
		   return 0;
   }
   
   public int getWidth()
   {
	   Window w = vc.getFullScreenWindow();
	   if(w != null)
		   return w.getWidth();
	   else
		   return 0;
   }
   
   //get out of full screen
   public void restoreScreen()
   {
	   Window w = vc.getFullScreenWindow();
	   if(w != null)
	   {
		   w.dispose();
		   
	   }
	   vc.setFullScreenWindow(null);
   }
   
 //create image compatible w/ monitor
   public BufferedImage createCompatibleImage(int w, int h, int t)
   {
   	  Window win = vc.getFullScreenWindow();
   	  if(win != null)
   	  {
   		  GraphicsConfiguration gc = win.getGraphicsConfiguration();
   		  return gc.createCompatibleImage(w, h, t);	  
   	  }
   	  return null;
   }

}

