package Control;

import java.util.ArrayList;

///\brief Class to hold the territories of the world map
public class Map
{
	public ArrayList<Territory> _territories = new ArrayList<Territory>();
	
	//Returns the teritory object with the given ID
	public Territory getTerritory(int id) throws IllegalArgumentException
	{
		if(id < 1 && id > Constants.NUMBER_OF_TERRITORIES) throw new IllegalArgumentException("Territory's Id must be between 1 and " + Constants.NUMBER_OF_TERRITORIES);
		for(Territory t : _territories)
		{
			if(t.getId() == id) return t;
		}
		return null;
	}
	
	//Initialize Map with all the default territories(Id, name, continent, neighbours)
	public void init()
	{
		_territories.clear();
		//Based on https://en.wikipedia.org/wiki/Risk_(game) -> Territories section
		ArrayList<Integer> nb = new ArrayList<Integer>();
		
		nb.clear(); nb.add(2); nb.add(6); nb.add(32);
		_territories.add(new Territory(1,  "Alaska", 				Continents.NorthAmerica, nb));
		
		nb.clear(); nb.add(1); nb.add(6); nb.add(7); nb.add(9);
		_territories.add(new Territory(2,  "Alberta", 				Continents.NorthAmerica, nb));
		
		nb.clear(); nb.add(4); nb.add(9); nb.add(13);
		_territories.add(new Territory(3,  "Central America", 		Continents.NorthAmerica, nb));
		
		nb.clear(); nb.add(3); nb.add(7); nb.add(8); nb.add(9);
		_territories.add(new Territory(4,  "Eastern United States", Continents.NorthAmerica, nb));
		
		nb.clear(); nb.add(6); nb.add(7); nb.add(8); nb.add(15);
		_territories.add(new Territory(5,  "Greenland", 			Continents.NorthAmerica, nb));
		
		nb.clear(); nb.add(1); nb.add(2); nb.add(5); nb.add(7);
		_territories.add(new Territory(6,  "Northwest Territory", 	Continents.NorthAmerica, nb));
		
		nb.clear(); nb.add(2); nb.add(4); nb.add(5); nb.add(6); nb.add(8); nb.add(9);
		_territories.add(new Territory(7,  "Ontario", 				Continents.NorthAmerica, nb));
		
		nb.clear(); nb.add(4); nb.add(5); nb.add(7);
		_territories.add(new Territory(8,  "Quebec", 				Continents.NorthAmerica, nb));
		
		nb.clear(); nb.add(2); nb.add(3); nb.add(4); nb.add(7);
		_territories.add(new Territory(9,  "Western United States", Continents.NorthAmerica, nb));
		
		
		nb.clear(); nb.add(11); nb.add(12);
		_territories.add(new Territory(10, "Argentina", 			Continents.SouthAmerica, nb));
		
		nb.clear(); nb.add(10); nb.add(12); nb.add(13); nb.add(25);
		_territories.add(new Territory(11, "Brazil", 				Continents.SouthAmerica, nb));
		
		nb.clear(); nb.add(10); nb.add(11); nb.add(13); 
		_territories.add(new Territory(12, "Peru", 					Continents.SouthAmerica, nb));
		
		nb.clear(); nb.add(3); nb.add(11); nb.add(12);
		_territories.add(new Territory(13, "Venezuela", 			Continents.SouthAmerica, nb));
		
		
		nb.clear(); nb.add(15); nb.add(16); nb.add(17); nb.add(20);
		_territories.add(new Territory(14, "Great Britain", 		Continents.Europe, nb));
		
		nb.clear(); nb.add(5); nb.add(14); nb.add(17);
		_territories.add(new Territory(15, "Iceland", 				Continents.Europe, nb));
		
		nb.clear(); nb.add(14); nb.add(17); nb.add(18); nb.add(19); nb.add(20);
		_territories.add(new Territory(16, "Northern Europe", 		Continents.Europe, nb));
		
		nb.clear(); nb.add(14); nb.add(15); nb.add(16); nb.add(19);
		_territories.add(new Territory(17, "Scandinavia", 			Continents.Europe, nb));
		
		nb.clear(); nb.add(16); nb.add(19); nb.add(20); nb.add(23); nb.add(25); nb.add(33);
		_territories.add(new Territory(18, "Southern Europe", 		Continents.Europe, nb));
		
		nb.clear(); nb.add(16); nb.add(17); nb.add(18); nb.add(27); nb.add(33); nb.add(37);
		_territories.add(new Territory(19, "Ukraine", 				Continents.Europe, nb));
		
		nb.clear(); nb.add(14); nb.add(16); nb.add(18); nb.add(25);
		_territories.add(new Territory(20, "Western Europe", 		Continents.Europe, nb));
		
		
		nb.clear(); nb.add(22); nb.add(25); nb.add(26);
		_territories.add(new Territory(21, "Congo", 				Continents.Africa, nb));
		
		nb.clear(); nb.add(21); nb.add(23); nb.add(24); nb.add(25); nb.add(26); nb.add(33);
		_territories.add(new Territory(22, "East Africa", 			Continents.Africa, nb));
		
		nb.clear(); nb.add(18); nb.add(22); nb.add(25); nb.add(33);
		_territories.add(new Territory(23, "Egypt", 				Continents.Africa, nb));
		
		nb.clear(); nb.add(22); nb.add(26);
		_territories.add(new Territory(24, "Madagascar", 			Continents.Africa, nb));
		
		nb.clear(); nb.add(11); nb.add(18); nb.add(20); nb.add(21); nb.add(22); nb.add(23);
		_territories.add(new Territory(25, "North Africa", 			Continents.Africa, nb));
		
		nb.clear(); nb.add(21); nb.add(22); nb.add(24);
		_territories.add(new Territory(26, "Egypt", 				Continents.Africa, nb));
		
		
		nb.clear(); nb.add(19); nb.add(28); nb.add(29); nb.add(33); nb.add(37);
		_territories.add(new Territory(27, "Afghanistan", 			Continents.Asia, nb));
		
		nb.clear(); nb.add(27); nb.add(29); nb.add(34); nb.add(35); nb.add(36); nb.add(37);
		_territories.add(new Territory(28, "China", 				Continents.Asia, nb));
		
		nb.clear(); nb.add(27); nb.add(28); nb.add(33); nb.add(35);
		_territories.add(new Territory(29, "India", 				Continents.Asia, nb));
		
		nb.clear(); nb.add(32); nb.add(34); nb.add(36); nb.add(38);
		_territories.add(new Territory(30, "Irkutsk", 				Continents.Asia, nb));
		
		nb.clear(); nb.add(32); nb.add(34);
		_territories.add(new Territory(31, "Japan", 				Continents.Asia, nb));
		
		nb.clear(); nb.add(1); nb.add(30); nb.add(31); nb.add(32); nb.add(34);
		_territories.add(new Territory(32, "Kamchatka", 			Continents.Asia, nb));
		
		nb.clear(); nb.add(18); nb.add(19); nb.add(23); nb.add(27); nb.add(29);
		_territories.add(new Territory(33, "Middle East", 			Continents.Asia, nb));
		
		nb.clear(); nb.add(28); nb.add(30); nb.add(31); nb.add(32); nb.add(36);
		_territories.add(new Territory(34, "Mongolia", 				Continents.Asia, nb));
		
		nb.clear(); nb.add(28); nb.add(29); nb.add(40);
		_territories.add(new Territory(35, "Siam", 					Continents.Asia, nb));
		
		nb.clear(); nb.add(28); nb.add(30); nb.add(34); nb.add(37); nb.add(38);
		_territories.add(new Territory(36, "Siberia", 				Continents.Asia, nb));
		
		nb.clear(); nb.add(19); nb.add(27); nb.add(28); nb.add(36);
		_territories.add(new Territory(37, "Ural", 					Continents.Asia, nb));
		
		nb.clear(); nb.add(30); nb.add(32); nb.add(36);
		_territories.add(new Territory(38, "Yakutsk", 				Continents.Asia, nb));
		
		
		nb.clear(); nb.add(41); nb.add(42);
		_territories.add(new Territory(39, "Eastern Australia", 	Continents.Australia, nb));
		
		nb.clear(); nb.add(35); nb.add(41);
		_territories.add(new Territory(40, "Indonesia", 			Continents.Australia, nb));
		
		nb.clear(); nb.add(39); nb.add(40); nb.add(42);
		_territories.add(new Territory(41, "New Guinea", 			Continents.Australia, nb));
		
		nb.clear(); nb.add(39); nb.add(41);
		_territories.add(new Territory(42, "Western Australia", 	Continents.Australia, nb));
	}
}
