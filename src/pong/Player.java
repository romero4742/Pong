package pong;

class Player 
{
	private int score;
	private String name;
	
	public Player()
	{
		score = 0;
		name = "";
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	protected void win()
	{
		score++;
	}
	public String toString()
	{
		return String.format(name);
	}
	public String getName()
	{
		return name;
	}
	public int getScore()
	{
		return score;
	}
	protected void setScore(int score)
	{
		this.score = score;
	}
	protected void playerWin()
	{
		score++;
	}
}