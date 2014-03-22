package pong;

import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Pong extends Core implements KeyListener,Runnable
{
	
   public static void main(String [] args)
   {
	   Pong p = new Pong();
	   p.run();
   }
   public void init()
   {
	  super.init();
	  Window w = s.getFullScreenWindow();
	  w.addKeyListener(this);  
   }
   
@Override
   public void draw(Graphics2D g)
   {
	  g.drawImage(bg, 0, 0,null);
	  g.drawImage(rightSprt.getImage(), Math.round(rightSprt.getX()), Math.round(rightSprt.getY()),null);
	  g.drawImage(leftSprt.getImage(),Math.round(leftSprt.getX()),Math.round(leftSprt.getY()),null);
	  g.drawImage(ballSprt.getImage(), Math.round(ballSprt.getX()), Math.round(ballSprt.getY()),null);
   }

private void selectingPlayer(int keyCode)
{
	if(keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN)
	{
		onePlayerChoice = !onePlayerChoice;
		return;
	}
	if(keyCode == KeyEvent.VK_ENTER)
	{
		selectingPlayer = false;
		return;
	}
}

private void loadingPlayers(int keyCode)
{
	if(keyCode == KeyEvent.VK_BACK_SPACE)
	{
		String name2 = currentPlayer.getName();
		if(name2.length() == 0)
		{
			keyCode = -1;
			return;
		}
		
		currentPlayer.setName(name2.substring(0,name2.length()-1));
		keyCode = -1;
		return;
	}
	if(keyCode == KeyEvent.VK_SPACE)
	{
		String name3 = currentPlayer.getName();
		if(name3.length() == 0)
		{
			keyCode = -1;
			return;
		}
		currentPlayer.setName(name3+" ");
		keyCode = -1;
		return;
	}
	if(keyCode == KeyEvent.VK_ENTER)
	{
		
		if(currentPlayer == player2)
		{
			if(currentPlayer.getName().length() == 0)
				currentPlayer.setName("Player 2");
			loadingPlayers = false;
			currentPlayer = null;
			return;
		}
		if(currentPlayer.getName().length() == 0)
			currentPlayer.setName("Player 1");
		currentPlayer = player2;
		keyCode = -1;
	}
	
	if(keyCode >= 96 && keyCode <= 111 || keyCode == 192 || keyCode == 222)
	{
		return;
	}
	if(Character.isLetter(keyCode))
	{
	   String name = currentPlayer.getName();
	   if(name.length() > 19)
	   {
		  return;
	   }
	   name += KeyEvent.getKeyText(keyCode);
	   currentPlayer.setName(name);
	   keyCode = -1;
	   return;
	}
	   keyCode = -1;
	   return;
}

public void keyPressed(KeyEvent e) 
{
	int keyCode = e.getKeyCode();
	e.consume();
	if(selectingPlayer)
	{
		selectingPlayer(keyCode);
		return;
	}
	if(loadingPlayers)
	{
		loadingPlayers(keyCode);
		return;
	}
	switch(keyCode)
	{
       case KeyEvent.VK_W:      leftPlayerMove(true); 
       					        break;
       case KeyEvent.VK_S:      leftPlayerMove(false);
                                break;
       case KeyEvent.VK_UP:	    rightPlayerMove(true);
       						    break;
       case KeyEvent.VK_DOWN:   rightPlayerMove(false);
							    break;						
       case KeyEvent.VK_ESCAPE: paused = false;
    	   						super.stop();  
       							break;
       case KeyEvent.VK_R:      super.resetGame();
    	    			        break;
       case KeyEvent.VK_P:      super.pause();
       					        break;
       case KeyEvent.VK_SPACE:  paused = false;
        						break;
       default: break;
	}
}

public void rightPlayerMove(boolean up)
{
   if(up)
   {
	   rightSprt.setVelocityY(-8f);
	   return;
   }
   rightSprt.setVelocityY(8f);
}

public void leftPlayerMove(boolean up)
{
	if(up)
	{
		leftSprt.setVelocityY(-8f);
		return;
	}
	leftSprt.setVelocityY(8f);
}

@Override
public void keyTyped(KeyEvent e) 
{
	e.consume();
}
	

@Override
public void keyReleased(KeyEvent e) 
{
	int keyCode = e.getKeyCode();
	switch(keyCode)
	{
	case KeyEvent.VK_UP: rightSprt.setVelocityY(0);
						 break;
	case KeyEvent.VK_DOWN: rightSprt.setVelocityY(0);
							break;
	case KeyEvent.VK_W: leftSprt.setVelocityY(0);
						break;
	case KeyEvent.VK_S: leftSprt.setVelocityY(0);
	   					break;
	  default: break; 
	}
	e.consume();
}
}