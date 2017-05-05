package Control;

import java.io.Serializable;
import java.util.ArrayList;

import Network.NetMsg;

public class GameState extends NetMsg implements Serializable
{
	public Boolean IsChanged = false;
	public Phases Phase;
	public ArrayList<Territory> ChangedTerritories;
	public int PlayerId;
	
	public GameState(Phases phase, ArrayList<Territory> changedTerritories, int playerId)
	{
		Phase = phase;
		
		if(changedTerritories == null) throw new NullPointerException("changedTerritories is null");
		ChangedTerritories = changedTerritories;
		
		PlayerId = playerId;
	}
}
