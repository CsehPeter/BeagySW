package Control;

import java.util.ArrayList;
import java.util.Random;

import Gui.Colors;
import Gui.Map;
import Gui.Territory;

//This class handles all the player interactions, the gmae flow is implemented here in it's own thread
public class Logic implements ICommand, Runnable
{
	//Id of the Server player
	private int _id = Constants.SERVER_PLAYER_ID;
	
	private ArrayList<Player> _players = new ArrayList<Player>();
	private ArrayList<IGameState> _games = new ArrayList<IGameState>();
	private Map _map;
	private GameState _gs;
	
	public Logic(IGameState game, Map map) throws NullPointerException
	{
		Player player = new Player(_id, Colors.NextColor());
		_players.add(player);
		
		if(map == null) throw new NullPointerException("map is null");
		_map = map;
		
		_gs = new GameState(Phases.Deploy, new ArrayList<Territory>(), player);
		
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
			t.Units = Constants.STARTING_UNITS;
			t.IsChanged = true;
		}
		
		_gs = new GameState(Phases.Deploy, new ArrayList<Territory>(), _players.get(_id));
		
		ChangeGs(_map);
		
//		//TODO show this?
//		for(Territory t : _map.Territories)
//		{
//			System.out.println("ID: " + t.getId() + "  Name: " + t.getName() + "  Continent: " + t.getContinent() + "  Owner: " + t.Owner.getId() + "  Units: " + t.Units);
//		}
		
		_gs.IsChanged = true;
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

		if(cmd.Player.getId() != _gs.Player.getId() && cmd.Click != CmdType.ClientConnected) return;
		
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
					if(_gs.Phase == Phases.Attack)
					{
						Attack(cmd);
					}
					if(_gs.Phase == Phases.Transfer)
					{
						Transfer(cmd);
					}
				}

				break;
				
			case Territory:
				if(_gs.Phase == Phases.Deploy)
				{
					if(cmd.ToId > 0 && cmd.ToId <= Constants.NUMBER_OF_TERRITORIES)
					{
						Deploy(cmd);
					}
				}
				break;
				
			default: break;
		}
	}
	
	private void ChangeGs(Map map)
	{
		//TODO GameState should only contain the changed territories
		synchronized(_gs)
		{
//			if(_gs == null)
//			{
//				_gs = new GameState(Phases.Deploy, new ArrayList<Territory>(), _players.get(_id));
//				_gs.ChangedTerritories = map.Territories;
//			}
			_gs.ChangedTerritories.clear();
			for(Territory t : map.Territories)
			{
				if(t.IsChanged == true)
				{
					_gs.ChangedTerritories.add(t);
					t.IsChanged = false;
				}
			}
			_gs.IsChanged = true;
		}
	}
	
	private void NextState()
	{
		switch(_gs.Phase)
		{
		
		case Attack:
			_gs.Phase = Phases.Transfer;
			_gs.IsChanged = true;
			break;
			
		case Deploy:
			_gs.Phase = Phases.Attack;
			_gs.IsChanged = true;
			break;
			
		case Transfer:
			//Next Player
			if( (_gs.Player.getId()) < _players.size() - 1)
			{
				_gs.Player = _players.get(_gs.Player.getId() + 1);
			}
				
			else
			{
				_gs.Player = _players.get(0);
			}
			_gs.Phase = Phases.Deploy;
			_gs.IsChanged = true;
			break;
			
		default: break;
		}
	}

	private void Deploy(Command cmd)
	{
		if(Support.IsFriendlyTerritory(cmd.Player.getId(), _map.getTerritory(cmd.ToId)))
		{
			_map.getTerritory(cmd.ToId).Units++;
			
			_map.getTerritory(cmd.ToId).IsChanged = true;
			
			ChangeGs(_map);
		}
	}
	
	private void Attack(Command cmd)
	{
		//int baseUnits = _map.getTerritory(cmd.FromId).Units;
		if(Support.CanAttack(cmd.Player.getId(), _map.getTerritory(cmd.FromId), _map.getTerritory(cmd.ToId), cmd.Units))
		{
			Support.Attack(cmd.Player.getId(), _map.getTerritory(cmd.FromId), _map.getTerritory(cmd.ToId), cmd.Units);
			
			ChangeGs(_map);
		}
		else
			System.out.println("Attack failed");
	}
	
	private void Transfer(Command cmd)
	{
		if(Support.CanTransfer(cmd.Player.getId(), _map, _map.getTerritory(cmd.FromId), _map.getTerritory(cmd.ToId)))
		{
			Support.Transfer(cmd.Player.getId(), _map, _map.getTerritory(cmd.FromId), _map.getTerritory(cmd.ToId), cmd.Units);
			
			ChangeGs(_map);
		}
		else
			System.out.println("Transfer failed");
	}	
	
	@Override
	public void run()
	{
		while(true)
		{
			synchronized(_gs)
			{
				if(_gs.IsChanged == true)
				{
					System.out.println("Game state is changed");
					_gs.IsChanged = false;
					
					for(Territory t : _gs.ChangedTerritories)
					{
						System.out.println("ID: " + t.getId() + "  Name: " + t.getName() + "  Continent: " + t.getContinent() + "  Owner: " + t.Owner.getId() + "  Units: " + t.Units);
					}
					
					for(IGameState game : _games)
					{
						game.OnGameState(new GameState(_gs.Phase, _gs.ChangedTerritories, _gs.Player));
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
