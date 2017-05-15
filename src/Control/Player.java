package Control;

import java.awt.Color;
import java.io.Serializable;

//Player class to identify different players of the game
public class Player implements Serializable
{
	private int _id;
	private Color _color;	
	
	/**
	 * @return The ID of the player
	 */
	public int getId()
	{
		return _id;
	}

	/**
	 * @return Color object which represent the player
	 */
	public Color getColor()
	{
		return _color;
	}
	
	//Default constructor initializes the object with ID zero and black color
	public Player()
	{
		_id = 0;
		_color = Color.black;
	}
	
	/**
	 * @param id ID of the new player, must be zero or higher
	 * @param color Color of the new player
	 * @throws IllegalArgumentException
	 * @throws NullPointerException
	 */
	public Player(int id, Color color) throws IllegalArgumentException, NullPointerException
	{
		if(id < 0) throw new IllegalArgumentException("Player's Id must be higher or equal to zero");
		_id = id;
		
		if(color == null) throw new NullPointerException("Player's color was null");
		_color = color;
	}
}