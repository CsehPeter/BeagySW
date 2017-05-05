package Control;

import java.util.ArrayList;

public class GameState
{
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
