package Network.Messages;

import java.io.Serializable;
import java.util.ArrayList;

import Control.Player;

public class ActionMsg extends NetMsg implements Serializable
{	
	private ArrayList<Control.Territory> _changedTerritories = new ArrayList<Control.Territory>();
	public ArrayList<Control.Territory> GetActionMsg()
	{
		return _changedTerritories;
	}
	
	public ActionMsg (Player player, ArrayList<Control.Territory> territories) throws IllegalArgumentException, NullPointerException
	{
		if(player == null) throw new NullPointerException("Player is null");
		_player = player;
		
		if(territories == null || territories.isEmpty()) throw new IllegalArgumentException("Territories was null or empty");
		this._changedTerritories = territories;
	}
}