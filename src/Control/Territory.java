package Control;

import java.io.Serializable;
import java.util.ArrayList;

//Class which store the data of a region
public class Territory implements Serializable
{
	private int _id;
	private String _name;
	private Continents _continent;
	private ArrayList<Integer> _neighbours = new ArrayList<Integer>();
	
	
	public Player Owner;
	public int Units;
	public Boolean IsChanged;
	
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
		if(id < 1 && id > Constants.NUMBER_OF_TERRITORIES) throw new IllegalArgumentException("Territory's Id must be between 1 and " + Constants.NUMBER_OF_TERRITORIES);
		_id = id;
		
		if(name == null || name.isEmpty()) throw new IllegalArgumentException("Territory's name was null or empty string");
		_name = name;
		
		_continent = continent;
		
		if(neighbours == null || neighbours.isEmpty()) throw new IllegalArgumentException("Territory's neighbours was null or empty string");
		_neighbours.clear();
		
		//Deep copy???
		for(int neighbourId : neighbours)
		{
			_neighbours.add(neighbourId);
		}
		
		Units = 0;
		IsChanged = false;
		Owner = new Player();
	}
	public static Territory Copy(Territory t)
	{
		Territory ret = new Territory(t._id, t._name, t._continent, t._neighbours);
		ret.IsChanged = t.IsChanged;
		ret.Owner = t.Owner;
		ret.Units = t.Units;
		
		return ret;
	}
}