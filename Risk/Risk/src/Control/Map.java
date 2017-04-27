package Control;

import java.util.ArrayList;

public class Map
{
	public ArrayList<Territory> _territories = new ArrayList<Territory>();
	
	//Initialize Map
	public void init()
	{
		ArrayList<Integer> neighbours = new ArrayList<Integer>();
		
		//North America -test
		neighbours.clear();
		neighbours.add(2); neighbours.add(6);
		_territories.add(new Territory(1, "Alaska", Continents.NorthAmerica, neighbours));
	}
}
