package Network;

import java.util.ArrayList;

public class ActionMsg extends NetMsg
{
	private ArrayList<Control.Territory> _changedTerritories = new ArrayList<Control.Territory>();

	public ArrayList<Control.Territory> getActionMsg()
	{
		return this._changedTerritories;
	}
	
	public ActionMsg (ArrayList<Control.Territory> territories) throws IllegalArgumentException
	{
		if(territories == null || territories.isEmpty()) throw new IllegalArgumentException("Argument territories was null or empty");
		this._changedTerritories = territories;
	}
}