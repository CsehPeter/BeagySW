package Control;

import java.util.ArrayList;
import java.util.Random;

import Gui.Colors;
import Gui.Map;
import Gui.Territory;

public class Logic implements ICommand, Runnable
{
	private int _id = 0;
	
	private ArrayList<Player> _players = new ArrayList<Player>();
	private ArrayList<IGameState> _games = new ArrayList<IGameState>();
	private Map _map;
	private GameState gs;
	
	public Logic(IGameState game, Map map) throws NullPointerException
	{
		Player player = new Player(_id, Colors.NextColor());
		_players.add(player);
		
		if(map == null) throw new NullPointerException("map is null");
		_map = map;
		
		gs = new GameState(Phases.Deploy, new ArrayList<Territory>(), player);
		
		if(game == null) throw new NullPointerException("game is null");
		_games.add(game);
		
		Thread th = new Thread(this);
		th.start();
	}
	
	public void InitMap()
	{		
		System.out.println("Randomize map");
		Random rnd = new Random();
		
		for(Territory t : _map.Territories)
		{
			int own = rnd.nextInt(_games.size());
			t.Owner = new Player(_players.get(own).getId(), _players.get(own).getColor());
			t.Units = 1;
		}
		
		gs = new GameState(Phases.Deploy, new ArrayList<Territory>(), _players.get(_id));
		ChangeGs(_map);
		
		for(Territory t : _map.Territories)
		{
			System.out.println("ID: " + t.getId() + "  Name: " + t.getName() + "  Continent: " + t.getContinent() + "  Owner: " + t.Owner.getId() + "  Units: " + t.Units);
		}
		
		gs.IsChanged = true;
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
		System.out.println(cmd.Click + " Player: " + cmd.Player.getId() + "  Units: " + cmd.Units + "  FromId: " + cmd.FromId + "  ToId: " +  cmd.ToId);

		if(cmd.Player.getId() != gs.Player.getId() && cmd.Click != Clicks.ClientConnected) return;
		
		switch(cmd.Click)
		{
			case ClientConnected:
				//_games.add(e)
				_players.add(new Player(_players.size(), Colors.NextColor()));
				InitMap();
				break;
			case Exit:
				System.out.println("Player " + cmd.Player.getId() + " disconnected");
				break;
				
			case Next:
				NextState();
				break;
				
			case Ok:
				if(cmd.FromId > 0 && cmd.FromId <= Constants.NUMBER_OF_TERRITORIES && cmd.ToId > 0 && cmd.ToId <= Constants.NUMBER_OF_TERRITORIES)
				{
					if(gs.Phase == Phases.Attack)
					{
						int baseUnits = _map.getTerritory(cmd.FromId).Units;
						if(Support.CanAttack(cmd.Player.getId(), _map.getTerritory(cmd.FromId), _map.getTerritory(cmd.ToId), cmd.Units))
						{
							Support.Attack(cmd.Player.getId(), _map.getTerritory(cmd.FromId), _map.getTerritory(cmd.ToId), cmd.Units);
							
							//Attacker won
							if(_map.getTerritory(cmd.ToId).Units == 0)
							{
								_map.getTerritory(cmd.ToId).Owner.setId(cmd.Player.getId());
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
						if(Support.CanTransfer(cmd.Player.getId(), _map, _map.getTerritory(cmd.FromId), _map.getTerritory(cmd.ToId)))
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
						if(Support.IsFriendlyTerritory(cmd.Player.getId(), _map.getTerritory(cmd.ToId)))
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
		gs.ChangedTerritories = map.Territories;
//		gs.ChangedTerritories.clear();
//		for(Territory t : map.Territories)
//		{
//			gs.ChangedTerritories.add(t);
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
			if( (gs.Player.getId()) < _players.size() - 1)
			{
				gs.Player = _players.get(gs.Player.getId() + 1);
			}
				
			else
			{
				gs.Player = _players.get(0);
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
			synchronized(gs)
			{
				if(gs.IsChanged == true)
				{
					System.out.println("Game state is changed");
					gs.IsChanged = false;
					
					for(IGameState game : _games)
					{
						game.OnGameState(new GameState(gs.Phase, gs.ChangedTerritories, gs.Player));
					}
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
