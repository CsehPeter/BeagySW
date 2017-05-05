package Control;

import java.util.ArrayList;

public class Logic implements ICommand, Runnable
{
	private ArrayList<IGameState> _games = new ArrayList<IGameState>();
	private Map _map = new Map();
	private GameState gs;
	
	public Logic(IGameState game) throws NullPointerException
	{
		_map.init();
		gs = new GameState(Phases.Deploy, _map._territories, 0);
		
		if(game == null) throw new NullPointerException("game is null");
		_games.add(game);
		
		Thread th = new Thread(this);
		th.start();
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

		if(cmd.PlayerId != gs.PlayerId) return;
		
		switch(cmd.Click)
		{
			case ClientConnected:
				//_games.add(e)
				break;
			case Exit:
				System.out.println("Player " + cmd.PlayerId + " disconnected");
				break;
				
			case Next:
				NextState();
				break;
				
			case Ok:
				if(cmd.FromId >= 0 && cmd.ToId >= 0)
				{
					if(Support.IsFriendlyTerritory(cmd.PlayerId, _map.getTerritory(cmd.FromId)))
					{
						//Transfer
						if(Support.IsFriendlyTerritory(cmd.PlayerId, _map.getTerritory(cmd.ToId)))
						{
							if(Support.CanTransfer(cmd.PlayerId, _map, _map.getTerritory(cmd.FromId), _map.getTerritory(cmd.ToId)))
							{
								_map.getTerritory(cmd.FromId).Units = _map.getTerritory(cmd.FromId).Units - cmd.Units;
								_map.getTerritory(cmd.ToId).Units = _map.getTerritory(cmd.ToId).Units + cmd.Units;
							}
							else
								System.out.println("Transfer failed");
						}
						else //Attack
						{
							
						}
					}
				}
				break;
				
			case Territory:
				break;
				
			default: break;
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
