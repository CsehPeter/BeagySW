package Control;

import java.util.ArrayList;

public class Logic implements ICommand, Runnable
{
	private ArrayList<IGameState> _games = new ArrayList<IGameState>();
	private GameState gs;
	
	public Logic()
	{
		Thread th = new Thread(this);
		th.start();
	}
	
	@Override
	public void OnCommand(Command cmd)
	{
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
