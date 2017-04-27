package Control;

import java.awt.Color;

public class Player
{
	private int _id = 0;
	private Color _color = Color.black;
	
	public int getId()
	{
		return _id;
	}
	public Color getColor()
	{
		return _color;
	}
	
	public Player(int id, Color color) throws IllegalArgumentException, NullPointerException
	{
		if(id < 0) throw new IllegalArgumentException("Player's Id must be higher than zero");
		_id = id;
		
		if(color == null) throw new NullPointerException("Player's color was null");
		_color = color;
	}
}