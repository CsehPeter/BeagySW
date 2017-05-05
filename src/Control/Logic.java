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
	
	@Override
	public void OnCommand(Command cmd)
	{
		System.out.println(cmd.PlayerId + "  " + cmd.Click + "  " + cmd.FromId + "  " +  cmd.ToId + "  " + cmd.Units);

		switch(cmd.Click)
		{
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
			gs.Phase = Phases.Deploy;
			break;
			
		case Deploy:
			gs.Phase = Phases.Attack;
			break;
			
		case Transfer:
			gs.Phase = Phases.Deploy;
			//Next Player
			if( (gs.PlayerId + 1) > (_games.size() - 1) )
				gs.PlayerId++;
			else
				gs.PlayerId = 0;
			break;
			
		default: break;
		}
	}

	@Override
	public void run()
	{
		while(true)
		{
			for(IGameState game : _games)
			{
					game.OnGameState(gs);
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
