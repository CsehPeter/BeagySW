package Control;

import java.util.ArrayList;
import java.util.Random;

public class Logic implements ICommand, Runnable
{
	private ArrayList<IGameState> _games = new ArrayList<IGameState>();
	private Map _map = new Map();
	private GameState gs;
	
	public Logic(IGameState game) throws NullPointerException
	{
		gs = new GameState(Phases.Deploy, new ArrayList<Territory>(), 0);
		if(game == null) throw new NullPointerException("game is null");
		_games.add(game);
		
		Thread th = new Thread(this);
		th.start();
	}
	
	public void InitMap()
	{
		_map = new Map();
		_map.init();
		
		System.out.println("Randomize map");
		Random rnd = new Random();
		
		for(Territory t : _map._territories)
		{
			int own = rnd.nextInt(_games.size());
			t.Owner.setId(own);
			t.Units = 1;
		}
		for(Territory t : _map._territories)
		{
			System.out.println("ID: " + t.getId() + "  Name: " + t.getName() + "  Continent: " + t.getContinent() + "  Owner: " + t.Owner.getId() + "  Units: " + t.Units);
		}
		
		gs = new GameState(Phases.Deploy, new ArrayList<Territory>(), 0);
		ChangeGs(_map);
		gs.IsChanged = true;
		
		System.out.println("hey");
		for(Territory t : _map._territories)
		{
			System.out.println("ID: " + t.getId() + "  Name: " + t.getName() + "  Continent: " + t.getContinent() + "  Owner: " + t.Owner.getId() + "  Units: " + t.Units);
		}
	}
	
	public void AddGame(IGameState game) throws NullPointerException
	{
		if(game == null) throw new NullPointerException("game is null");
		
		synchronized(_games)
		{
			_games.add(game);
		}
	}
	
	@Override
	public void OnCommand(Command cmd)
	{
		System.out.println(cmd.PlayerId + "  " + cmd.Click + "  " + cmd.FromId + "  " +  cmd.ToId + "  " + cmd.Units);

		if(cmd.PlayerId != gs.PlayerId && cmd.Click != Clicks.ClientConnected) return;
		
		switch(cmd.Click)
		{
			case ClientConnected:
				//_games.add(e)
				InitMap();
				break;
			case Exit:
				System.out.println("Player " + cmd.PlayerId + " disconnected");
				break;
				
			case Next:
				NextState();
				for(Territory t : _map._territories)
				{
					System.out.println(t.Owner.getId());
				}
				break;
				
			case Ok:
				if(cmd.FromId > 0 && cmd.FromId <= Constants.NUMBER_OF_TERRITORIES && cmd.ToId > 0 && cmd.ToId <= Constants.NUMBER_OF_TERRITORIES)
				{
					if(gs.Phase == Phases.Attack)
					{
						int baseUnits = _map.getTerritory(cmd.FromId).Units;
						if(Support.CanAttack(cmd.PlayerId, _map.getTerritory(cmd.FromId), _map.getTerritory(cmd.ToId), cmd.Units))
						{
							Support.Attack(cmd.PlayerId, _map.getTerritory(cmd.FromId), _map.getTerritory(cmd.ToId), cmd.Units);
							
							//Attacker won
							if(_map.getTerritory(cmd.ToId).Units == 0)
							{
								_map.getTerritory(cmd.ToId).Owner.setId(cmd.PlayerId);
								_map.getTerritory(cmd.ToId).Units = _map.getTerritory(cmd.FromId).Units - (baseUnits - cmd.Units); //20 -> 15 -> 7 -> 2
							}
							ChangeGs(_map);
							gs.IsChanged = true;
						}
						else
							System.out.println("Attack failed");
					}
					if(gs.Phase == Phases.Transfer)
					{
						if(Support.CanTransfer(cmd.PlayerId, _map, _map.getTerritory(cmd.FromId), _map.getTerritory(cmd.ToId)))
						{
							_map.getTerritory(cmd.FromId).Units = _map.getTerritory(cmd.FromId).Units - cmd.Units;
							_map.getTerritory(cmd.ToId).Units = _map.getTerritory(cmd.ToId).Units + cmd.Units;
							ChangeGs(_map);
							gs.IsChanged = true;
						}
						else
							System.out.println("Transfer failed");
					}
				}

				break;
				
			case Territory:
				if(gs.Phase == Phases.Deploy)
				{
					if(cmd.ToId > 0 && cmd.ToId <= Constants.NUMBER_OF_TERRITORIES)
					{
						if(Support.IsFriendlyTerritory(cmd.PlayerId, _map.getTerritory(cmd.ToId)))
						{
							_map.getTerritory(cmd.ToId).Units++;
							ChangeGs(_map);
							gs.IsChanged = true;
						}
					}
				}
				break;
				
			default: break;
		}
	}
	
	private void ChangeGs(Map map)
	{
		gs.ChangedTerritories = map._territories;
//		for(Territory t : map._territories)
//		{
//			Territory tmp = new Territory(t.getId(), t.getName(), t.getContinent(), t.getNeighbours());
//			tmp.Owner = t.Owner;
//			tmp.Units = t.Units;
//			gs.ChangedTerritories.add(tmp);
//		}
	}
	
	private void ChangeMap(GameState gs)
	{
		for(Territory t : gs.ChangedTerritories)
		{
			_map.getTerritory(t.getId()).Owner = t.Owner;
			_map.getTerritory(t.getId()).Units = t.Units;
		}
	}
	
	private void NextState()
	{
		switch(gs.Phase)
		{
		
		case Attack:
			gs.Phase = Phases.Transfer;
			gs.IsChanged = true;
			break;
			
		case Deploy:
			gs.Phase = Phases.Attack;
			gs.IsChanged = true;
			break;
			
		case Transfer:
			//Next Player
			if( (gs.PlayerId + 1) < _games.size() )
			{
				gs.PlayerId++;
			}
				
			else
			{
				gs.PlayerId = 0;
			}
			gs.Phase = Phases.Deploy;
			gs.IsChanged = true;
			break;
			
		default: break;
		}
	}

	@Override
	public void run()
	{
		while(true)
		{
			if(gs.IsChanged == true)
			{
				System.out.println("Change");
				gs.IsChanged = false;
				
				for(IGameState game : _games)
				{
						game.OnGameState(new GameState(gs.Phase, gs.ChangedTerritories, gs.PlayerId));
				}
			}
			
			try
			{
				Thread.sleep(5);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
