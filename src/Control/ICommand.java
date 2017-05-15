package Control;

//Interface for classes which react when a player has made an interaction
public interface ICommand
{
	public void OnCommand(Command cmd);
}
