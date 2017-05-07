package Control;

import java.io.Serializable;
import java.util.ArrayList;

import Network.NetMsg;

public class GameState extends NetMsg implements Serializable
{
	public Boolean IsChanged = false;
	public Phases Phase;
	public ArrayList<Territory> ChangedTerritories;
	public Player Player;
	
	public GameState(Phases phase, ArrayList<Territory> changedTerritories, Player player)
	{
		Phase = phase;
		
		if(changedTerritories == null) throw new NullPointerException("changedTerritories is null");
		ChangedTerritories = changedTerritories;
		
		if(player == null) throw new NullPointerException("Player is null");
		Player = player;
	}
}
