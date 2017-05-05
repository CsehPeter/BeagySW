package Control;

import java.util.ArrayList;
import java.util.HashMap;

import Gui.Colors;
import Network.Messages.*;

public class ServerGame extends Game
{
	private ArrayList<Player> _players = new ArrayList<Player>();
	private HashMap<Player, GameStateType> status = new HashMap<Player, GameStateType>();
	private int currentPlayerNumber = 0;
	
	public ServerGame()
	{
		//_players.add(player);
		status.put(player, GameStateType.Connected);
		
		Thread th = new Thread(this);
		th.start();
	}
	
	public void ConnectedState()
	{
		StatusMsg msg; 
		
		msg = network.GetStatusMsg(StatusMsgType.Connected, true);
		if(msg != null)
		{
			changePlayerState(msg.getPlayer(), GameStateType.Connected);
		}
		
		msg = network.GetStatusMsg(StatusMsgType.Ready, true);
		if(msg != null)
		{
			changePlayerState(msg.getPlayer(), GameStateType.Ready);
		}
		
	}
	
	public void Ready()
	{
		changePlayerState(player, GameStateType.Ready);
		gameState = GameStateType.Ready;
	}
	
	public void ReadyState()
	{
		StatusMsg msg; 
		
		//Check if new player connects
		msg = network.GetStatusMsg(StatusMsgType.Connected, true);
		if(msg != null)
		{
			changePlayerState(msg.getPlayer(), GameStateType.Connected);
		}
		
		//Check if a player sends ready signal
		msg = network.GetStatusMsg(StatusMsgType.Ready, true);
		if(msg != null)
		{
			changePlayerState(msg.getPlayer(), GameStateType.Ready);
		}
		
		//Check if all players are ready
		int readyPlayers = 0;
		for(GameStateType state : status.values())
		{
			if(state == GameStateType.Ready)
			{
				readyPlayers++;
			}
		}
		//At least 2 players in the game, and everyone is ready
		if(readyPlayers >= 2 && readyPlayers == status.size())
		{
			StartGame();
		}
	}
	
	//generate player order, colors, send allplayerready signal, wait -> then send playerturn signal for the first player
	private void StartGame()
	{
		for(int i = 0; i < status.size(); i++)
		{
			Player p = new Player( (i + 1), Colors.NextColor());
			network.send(new StatusMsg(p, StatusMsgType.PlayerInfo));
			network.send(new StatusMsg(p, StatusMsgType.AllPlayerReady));
			
			changePlayerState(p, GameStateType.Wait);
		}
		
		gameState = GameStateType.Deploy; //MINDIG A SZERVER KEZD!!!
	}
	
	//private Player NextPlayer()
	
	public void WaitState()
	{
		
	}
	public void DeployState()
	{
		
	}
	public void AttackState()
	{
		
	}
	public void TransferState()
	{
		
	}
	public void WinState()
	{
		
	}
	public void DefeatState()
	{
		
	}
	
	private void changePlayerState(Player player, GameStateType gameState) throws NullPointerException
	{
		if(player == null) throw new NullPointerException("player is null");
		
		if(status.containsKey(player)) status.remove(player);
		status.put(player, gameState);
	}
	
}
