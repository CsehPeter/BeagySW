package Control;

public class Logic implements ICommand
{

	@Override
	public void OnCommand(Command cmd)
	{
		if(cmd.Click == Clicks.Ok)
		{
			//Support.Attack(cmd.PlayerId, cmd.FromId, cmd.ToId, cmd.Units);
		}
	}

}
