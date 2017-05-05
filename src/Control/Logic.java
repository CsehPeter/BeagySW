package Control;

import java.util.ArrayList;

public class Logic implements ICommand, Runnable
{
	private ArrayList<IGameState> _games = new ArrayList<IGameState>();
	private GameState gs;
	
	public Logic(IGameState game) throws NullPointerException
	{
		if(game == null) throw new NullPointerException("game is null");
		_games.add(game);
		
		Thread th = new Thread(this);
		th.start();
	}
	
	@Override
	public void OnCommand(Command cmd)
	{
		System.out.print(cmd.PlayerId + "  " + cmd.Click + "  " + cmd.FromId + "  " +  cmd.ToId + "  " + cmd.Units);
		
		
		if(cmd.Click == Clicks.Ok)
		{
			//Support.Attack(cmd.PlayerId, cmd.FromId, cmd.ToId, cmd.Units);
		}
	}

	@Override
	public void run()
	{
		while(true)
		{
			for(IGameState game : _games)
			{
				if(gs != null)
				{
					game.OnGameState(gs);
				}
			}
		}
	}

}
