package Control;

import java.io.Serializable;
import java.util.ArrayList;

import Gui.Territory;
import Network.NetMsg;

//Serializable class which contains the information about the changed game state 
public class GameState extends NetMsg implements Serializable
{
	public Boolean IsChanged = false;
	public Phases Phase;
	public ArrayList<Territory> ChangedTerritories;
	public Player Player;
	
	/**
	 * @param phase Phase of the player's turn
	 * @param changedTerritories ArrayList of territories which are change in the last interaction
	 * @param player Represents the active player
	 */
	public GameState(Phases phase, ArrayList<Territory> changedTerritories, Player player)
	{
		Phase = phase;
		
		if(changedTerritories == null) throw new NullPointerException("changedTerritories is null");
		ChangedTerritories = changedTerritories;
		
		if(player == null) throw new NullPointerException("Player is null");
		Player = player;
	}
}
