package Control;

//Interface for classes which are want to get notified when the game state changes
public interface IGameState
{
	public void OnGameState(GameState gs);
}
