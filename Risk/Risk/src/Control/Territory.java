package Control;

import java.util.ArrayList;

public class Territory
{
	//"read only" variables
	private int _id;
	private String _name;
	private Continents _continent;
	private ArrayList<Integer> _neighbours = new ArrayList<Integer>();
	
	
	public Player Owner;
	public int Units;
	
	public int getId()
	{
		return _id;
	}
	public String getName()
	{
		return _name;
	}
	public Continents getContinent()
	{
		return _continent;
	}
	public ArrayList<Integer> getNeighbours()
	{
		return _neighbours;
	}
	
	public Territory(int id, String name, Continents continent, ArrayList<Integer> neighbours)
	{
		if(id < 0) throw new IllegalArgumentException("Territory's Id must be higher than zero");
		_id = id;
		
		if(name == null || name.isEmpty()) throw new IllegalArgumentException("Territory's name was null or empty string");
		_name = name;
		
		_continent = continent;
		
		if(neighbours == null || neighbours.isEmpty()) throw new IllegalArgumentException("Territory's neighbours was null or empty string");
		_neighbours = neighbours;
		
		Units = 0;
	}
}