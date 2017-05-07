package Control;

import java.awt.Color;
import java.io.Serializable;

public class Player implements Serializable
{
	private int _id;
	private Color _color;	
	
	public int getId()
	{
		return _id;
	}
	public void setId(int id)
	{
		_id = id;
	}
	public Color getColor()
	{
		return _color;
	}
	
	public Player()
	{
		_id = 0;
		_color = Color.black;
	}
	public Player(int id, Color color) throws IllegalArgumentException, NullPointerException
	{
		if(id < 0) throw new IllegalArgumentException("Player's Id must be higher than zero");
		_id = id;
		
		if(color == null) throw new NullPointerException("Player's color was null");
		_color = color;
	}
}