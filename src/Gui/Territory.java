package Gui;
import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.io.Serializable;
import java.util.ArrayList;

import Control.Constants;
import Control.Continents;
import Control.Player;

public class Territory implements Serializable
{
	private int _id;
	private String _name;
	private Continents _continent;
	private Shape _shape;
	private Point _displayPos;
	private ArrayList<Integer> _neighbours = new ArrayList<Integer>();
	
	public int Units;
	public Player Owner;
	public Boolean IsChanged;
	
	public Territory(int id, String name, Continents continent, ArrayList<Integer> neighbours, Shape sh, Point pos) throws NullPointerException, IllegalArgumentException
	{
		if(id < 1 && id > Constants.NUMBER_OF_TERRITORIES) throw new IllegalArgumentException("Territory's Id must be between 1 and " + Constants.NUMBER_OF_TERRITORIES);
		_id = id;
		
		if(name == null || name.isEmpty()) throw new IllegalArgumentException("Territory's name was null or empty string");
		_name = name;
		
		_continent = continent;
		
		if(neighbours == null || neighbours.isEmpty()) throw new IllegalArgumentException("Territory's neighbours was null or empty");
		_neighbours.clear();
		
		//Deep copy???
		for(int neighbourId : neighbours)
		{
			_neighbours.add(neighbourId);
		}
		
		if(pos == null) throw new NullPointerException("Territory's position was null");
		_displayPos = pos;
		
		if(sh == null) throw new NullPointerException("Territory's shape was null");
		_shape = sh;
		
		Units = 0;
		IsChanged = false;
	}
	
	public int getId()
	{
		return _id;
	}

	public String getName()
	{
		return _name;
	}
	
	public Color getFillColor()
	{
		if(Owner != null)
			return Owner.getColor();
		else
			return Color.GRAY;
	}

	public Continents getContinent()
	{
		return _continent;
	}
	public ArrayList<Integer> getNeighbours()
	{
		return _neighbours;
	}

	public Shape getShape()
	{
		return _shape;
	}
	
	public int getX()
	{
		return _displayPos.x;
	}
	public int getY()
	{
		return _displayPos.y;
	}
}
