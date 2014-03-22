package pong;

import java.awt.*;
import javax.swing.*;

abstract class Core 
{


	private static final DisplayMode modes1[] = {
												 new DisplayMode(1366,768,32,0),
												 new DisplayMode(1366,768,24,0),
												 new DisplayMode(1366,768,16,0),
												 new DisplayMode(800,600,32,0),
			  									 new DisplayMode(800,600,24,0),
			  									 new DisplayMode(800,600,16,0),
			  									 new DisplayMode(800,480,32,0),
			  									 new DisplayMode(800,480,24,0),
			  									 new DisplayMode(800,480,16,0)};
	protected boolean running;
	protected boolean paused;
	protected ScreenManager s;
	protected Image bg,left,right,ball;
	protected Sprite leftSprt,rightSprt,ballSprt;
	protected Player player1,player2,currentPlayer;
	protected String exit,pause,begin,createdBy,contact,askNames,name1,name2,newGame,copyRight,onePlayer,twoPlayers;
	protected boolean loadingPlayers,selectingPlayer,onePlayerChoice;
	private double maxBarUpdate;
	private double leftBarMarker,rightBarMarker;
	private Computer computer;
	
	//call initialize and loop
   public void run()
   {
	  try
	  {
		 init();
	 	 initializeGraphics();
	 	 Graphics2D g = s.getGraphics();
	 	 drawLoadingNames(g);
	 	 g.dispose();
	 	 playerSelection();
		 loadPlayers();
		 if(onePlayerChoice)
		 {
			computer = new Computer(s.getGraphicsDevice());
			computer.setScreenWidth(s.getWidth(),s.getHeight());
	 	    onePlayerGameLoop();
		 }
		 else
		 {
			twoPlayerGameLoop(); 
		 }
	  }
	  finally
	  {
	 	 s.restoreScreen();
	  }
	} 
   
    public void playerSelection()
    {
    	selectingPlayer = true;
    	onePlayer = "1 Player";
    	twoPlayers = "2 Players";
    	onePlayerChoice = true;
    	while(selectingPlayer)
    	{
    	   try
    	   {
    	      Graphics2D g = s.getGraphics();
    	      drawPlayerSelection(g);
    	      g.dispose();
    	      s.update();
    	   }
    	   catch(Exception e)
    	   {
    		  System.out.println("Error in selecting players!!!");
    		  e.printStackTrace();
    	   }
    	}
    }
	
	public void init()
	{
		s = new ScreenManager();
		DisplayMode dm = s.findFirstCompatibleMode(modes1);
		s.setFullScreen(dm);
		running = true;
	}

	public void initializeGraphics()
	{
	   bg = new ImageIcon(getClass().getClassLoader().getResource("bg.png")).getImage();
	   left = new ImageIcon(getClass().getClassLoader().getResource("left.png")).getImage();
	   right = new ImageIcon(getClass().getClassLoader().getResource("Right.png")).getImage();
	   ball = new ImageIcon(getClass().getClassLoader().getResource("Ball.png")).getImage();
	   
	   
	   leftSprt = new Sprite(left);
	   leftSprt.setX(0);
	   leftSprt.setY(s.getHeight()/2-left.getHeight(null)/2);
	   leftSprt.setVelocityY(0);
	   rightSprt = new Sprite(right);
	   rightSprt.setX(s.getWidth()-right.getWidth(null));
	   rightSprt.setY(s.getHeight()/2-right.getHeight(null)/2);
	   rightSprt.setVelocityY(0);
	   ballSprt = new Sprite(ball);
	   ballSprt.setX(s.getWidth()/2 - ballSprt.getWidth()/2);
	   ballSprt.setY(s.getHeight()/2-ballSprt.getHeight()/2);
	   exit = "Press <esc> to exit";
	   askNames = "Type names...Press <Enter> to continue";
	   pause ="Press <p> to pause\n";
	   begin ="Press <Space_Bar> to begin";
	   newGame = "Press <r> to reset game";
	   createdBy = "Game Created by Victor Romero";
	   contact = "romero4742@hotmail.com";
	   copyRight = "Copyright © 2012";
	   name1 = "";
	   name2 = "";
	   maxBarUpdate = s.getWidth() / 4;
	   leftBarMarker = leftSprt.getX();
	   rightBarMarker = rightSprt.getX()+rightSprt.getWidth();
	   player1 = new Player();
	   player2 = new Player();
	}
   
	public void stop()
	{
		running = false;
	}
	
	public void pause()
	{
		paused = !paused;
	}
	
	
	public void loadPlayers()
	{
		loadingPlayers = true;
		currentPlayer = player1;
		loadingLoop();
	}
 
	
	public void loadingLoop()
	{
		while(loadingPlayers)
		{
		   Graphics2D g = s.getGraphics();
		   drawLoadingNames(g);
		   g.dispose();
		   s.update();
		}
	}
	
	public void onePlayerGameLoop()
	{
		paused = true;
		checkPause();
		computer.setRightPosition(rightSprt.getX());
		startBall();
		long startTime = System.currentTimeMillis(); //track of time when started
		int timeTarget = 7;    					 // keeps number of seconds until update
		while(running)
		{
			checkPause();
			Graphics2D g = s.getGraphics();
			draw(g);
			float firstX = ballSprt.getX();
			float firstY = ballSprt.getY();
			
			checkPosition();
			g.dispose();
			s.update();
			rightSprt.update();
			leftSprt.update();
			ballSprt.update();
			float secondX = ballSprt.getX();
			float secondY = ballSprt.getY();
			computer.calculatePosition(firstX,firstY,secondX,secondY);
			computer.computerMove(rightSprt.getY()+rightSprt.getHeight()/2);
			int currTime = (int) (System.currentTimeMillis() - startTime)/1000;
			if(currTime == timeTarget)
			{
				updateBarPositions();
				timeTarget += 5;
			}
			try
			{
			   Thread.sleep(30);
			}
			catch(Exception e)
			{}
			
		}
		if(loadingPlayers)
		{
			loadPlayers();
			running = true;
		    onePlayerGameLoop();	
		}
	
	}
	
	public void twoPlayerGameLoop()
	{
		paused = true;
		checkPause();
		startBall();
		long startTime = System.currentTimeMillis(); //track of time when started
		int timeTarget = 7;    					 // keeps number of seconds until update
		while(running)
		{
			int currTime = (int) (System.currentTimeMillis() - startTime)/1000;
			if(currTime == timeTarget)
			{
				updateBarPositions();
				timeTarget += 5;
			}
			checkPause();
			Graphics2D g = s.getGraphics();
			draw(g);
			checkPosition();
			g.dispose();
			s.update();
			rightSprt.update();
			leftSprt.update();
			ballSprt.update();
			try
			{
			   Thread.sleep(30);
			}
			catch(Exception e)
			{}
			
		}
		if(loadingPlayers)
		{
			loadPlayers();
			running = true;
		    twoPlayerGameLoop();	
		}
	}

	public void updateBarPositions()
	{
		if(leftSprt.getX() < maxBarUpdate)
		{
		   float leftCurrPosition = leftSprt.getX();
		   float rightCurrPosition = rightSprt.getX();
		   leftSprt.setX(leftCurrPosition+leftSprt.getWidth());
		   rightSprt.setX(rightCurrPosition-rightSprt.getWidth());
		   leftBarMarker = leftSprt.getX();
		   rightBarMarker = rightSprt.getX()+rightSprt.getWidth();
		}
	}
	
	public void checkPause()
	{
		while(paused)
		{
			
			Graphics2D g = s.getGraphics();
			pauseDraw(g);
			g.dispose();
			s.update();
		}
	}
	
	public void startBall()
	{
	   ballSprt.setVelocityX(10);
	   ballSprt.setVelocityY(10);
	}

	
	public void resetScreen()
	{
		ballSprt.setX(s.getWidth()/2 - ballSprt.getWidth()/2);
		ballSprt.setY(s.getHeight()/2-ballSprt.getHeight()/2);
		leftSprt.setX(0);
		leftSprt.setY(s.getHeight()/2-left.getHeight(null)/2);
		rightSprt.setX(s.getWidth()-right.getWidth(null));
		rightSprt.setY(s.getHeight()/2-right.getHeight(null)/2);
		leftBarMarker = leftSprt.getX();
		rightBarMarker = rightSprt.getX()+rightSprt.getWidth();
		if(onePlayerChoice)
			onePlayerGameLoop();
		else
	        twoPlayerGameLoop();
	}
	
	public void resetGame()
	{
		player1.setName("");
		player2.setName("");
		player1.setScore(0);
		player2.setScore(0);
		running = false;
		loadingPlayers = true;
		paused = false;
	}
	
	public void checkPosition()
	   {
		   if(ballSprt.getX() <= leftBarMarker)
			{
			   playerWin(player2);
			   return;
			}
			if(ballSprt.getX() > rightBarMarker)
			{
					playerWin(player1);
					return;
			}
			//check collision w/ left bar
			if((ballSprt.getX() <= leftSprt.getX()+leftSprt.getWidth())&& (ballSprt.getX() >= ballSprt.getWidth()/2) && (ballSprt.getY() > leftSprt.getY()-ballSprt.getHeight()) &&( ballSprt.getY() < leftSprt.getY()+leftSprt.getHeight()+ballSprt.getHeight()))
			{
				   ballSprt.setVelocityX(Math.abs(ballSprt.getVelocityX()));
			}
			//check collision w/ right bar
			if((ballSprt.getX()+ballSprt.getWidth() >= rightSprt.getX())&&(ballSprt.getX()+ballSprt.getWidth() <= rightSprt.getX()+rightSprt.getWidth()/2 )&&(ballSprt.getY()+ballSprt.getHeight() > rightSprt.getY())&&(ballSprt.getY() < rightSprt.getY()+rightSprt.getHeight()))
			{
				ballSprt.setVelocityX(-(ballSprt.getVelocityX()));
			}
			if(leftSprt.getY() < 0)
		    {
			   leftSprt.setY(0);
			}
			if(leftSprt.getY() + leftSprt.getHeight() > s.getHeight())
			{
			   leftSprt.setY(s.getHeight()-leftSprt.getHeight());
			}
			if(rightSprt.getY() < 0)
		    {
			   rightSprt.setY(0);
			}
			if(rightSprt.getY() + rightSprt.getHeight() > s.getHeight())
			{
			   rightSprt.setY(s.getHeight()-rightSprt.getHeight());
			}
			if(ballSprt.getY()+ballSprt.getHeight() < 0)
			{
				ballSprt.setVelocityY(Math.abs(ballSprt.getVelocityY()));
			}
			if(ballSprt.getY() >= s.getHeight()-ballSprt.getHeight())
			{
				ballSprt.setVelocityY(-ballSprt.getVelocityY());
			}
	   }
	
	public void playerWin(Player player)
	{
       player.win();
       resetScreen();
	}
	
	public void drawPlayerSelection(Graphics2D g)
	{
		g.drawImage(bg,0,0,null);
		if(onePlayerChoice)
		{
		   g.setFont(new Font("SERIF",Font.PLAIN,25));
		   g.drawString(twoPlayers, s.getWidth()/2-145, s.getHeight()/2);
		   g.setFont(new Font("SERIF", Font.BOLD,30));
		   g.drawString(onePlayer, s.getWidth()/2-145, s.getHeight()/2-50);
		   g.dispose();
		}
		else
		{
			g.setFont(new Font("SERIF",Font.PLAIN,25));
			g.drawString(onePlayer, s.getWidth()/2-145, s.getHeight()/2-50);
			g.setFont(new Font("SERIF", Font.BOLD,30));
			g.drawString(twoPlayers, s.getWidth()/2-145, s.getHeight()/2);
			g.dispose();
		}
	}
	
	public void drawLoadingNames(Graphics2D g)
	{
		g.drawImage(bg, 0, 0,null);
		g.drawImage(rightSprt.getImage(), Math.round(rightSprt.getX()), Math.round(rightSprt.getY()),null);
	    g.drawImage(leftSprt.getImage(),Math.round(leftSprt.getX()),Math.round(leftSprt.getY()),null);
	    g.drawImage(ballSprt.getImage(), Math.round(ballSprt.getX()), Math.round(ballSprt.getY()),null);
	    
	    g.setFont(new Font("SERIF",Font.BOLD,20));
	    s.getFullScreenWindow().setForeground(Color.CYAN);
	    g.drawString(player1.getName(),300,50);
	    g.drawString(player2.getName(),800,50);
	    g.drawString(askNames, 520, 260);
	    g.drawString(createdBy, s.getWidth()/2-145, s.getHeight()-100);
	    g.drawString(contact, s.getWidth()/2-125, s.getHeight()-75);
	    g.drawString(copyRight, s.getWidth()/2-155, s.getHeight()-50);
	    g.dispose();
	}
	
	public void pauseDraw(Graphics2D g)
	{
		g.drawImage(bg, 0, 0,null);
		g.drawImage(rightSprt.getImage(), Math.round(rightSprt.getX()), Math.round(rightSprt.getY()),null);
	    g.drawImage(leftSprt.getImage(),Math.round(leftSprt.getX()),Math.round(leftSprt.getY()),null);
	    g.drawImage(ballSprt.getImage(), Math.round(ballSprt.getX()), Math.round(ballSprt.getY()),null);
	    
	    g.setFont(new Font("SERIF",Font.BOLD,20));
	    s.getFullScreenWindow().setForeground(Color.CYAN);
	    g.drawString(player1.getName(),300,50);
	    String score = "";
	    score += player1.getScore();
	    g.drawString(score, 430,70);
	    score = "";
	    score += player2.getScore();
	    g.drawString(player2.getName(),800,50);
	    g.drawString(score,930,70);
	    g.drawString(exit, 585, 260);
	    g.drawString(pause, 585, 280);
	    g.drawString(newGame, 585, 300);
	    g.drawString(begin, 585, 320);
	    g.drawString(createdBy, s.getWidth()/2-145, s.getHeight()-100);
	    g.drawString(contact, s.getWidth()/2-125, s.getHeight()-75);
	    g.drawString(copyRight, s.getWidth()/2-155, s.getHeight()-50);
	    g.dispose();
	}
	
	public abstract void draw(Graphics2D g);

}

