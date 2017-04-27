package Control;

import java.util.ArrayList;

import Network.*;

//Controls the main flow of the game by using a state machine
public class Control implements Runnable
{
	private GameState _gameState;
	private Network _network;
	private Player _player;
	private Map _map;
	
	public Control()
	{
		_gameState = GameState.Started;
	}
	
	//State machine for game sections
	//Runs in an infinite loop
	public void Play() 
	{
		while(true)
		{
			//System.out.println("hey0");
			switch(_gameState)
			{
				case Started 	: Started(); break;
				case NetworkSet : NetworkSet(); break;
				case Ready 		: Ready(); break;
				case Wait 		: Wait(); break;
				case Deploy 	: Deploy(); break;
				case Attack 	: Attack(); break;
				case Transfer 	: Transfer(); break;
				case Win 		: Win(); break;
				case Defeat 	: Defeat(); break;
			}
		}
	}

	//Waiting for the player to start the network
	private void Started()
	{
		if(_network != null) _gameState = GameState.NetworkSet;
	}
	
	//Waiting for the player to send the ready signal
	private void NetworkSet()
	{
		System.out.println("hey2");
		//waiting for user to click ready
		//when it's done...
		_network.send(new StatusMsg(0, StatusMsgType.Ready)); //itt még nincs playerID
	}
	
	//Player is ready, waiting for all the players to be ready, and send the AllPlayerReady signal
	private void Ready()
	{
		
		if(_network.SearchMsg(StatusMsgType.AllPlayerReady))
		{
			_gameState = GameState.Wait;
		}
	}
	
	//Waiting for the player's turn
	private void Wait()
	{
		//Check if defeated
		if(_network.SearchMsg(StatusMsgType.Defeat))
		{
			_gameState = GameState.Defeat;
		}
		
		//Update map
		ArrayList<ActionMsg> msgs = _network.GetActionMsgs();
		if(msgs.isEmpty() == false) //new msg
		{
			UpdateMap(msgs);
		}
		
		//Wait for turn started
		if(_network.SearchMsg(StatusMsgType.TurnStarted))
		{
			_gameState = GameState.Deploy;
		}
	}
	
	//Deploy phase of the player
	private void Deploy()
	{
		
	}
	
	//Attack phase of the player
	private void Attack()
	{
		
	}
	
	//Transfer phase of the player
	private void Transfer()
	{
		
	}
	
	//Player won the game
	private void Win()
	{
		
	}
	
	//Player lost the game
	private void Defeat()
	{
		
	}
	
	//Start Network
	public void StartServer()
	{
		if (_network != null)
			_network.disconnect();
		_network = new SerialServer(this);
		_network.connect("localhost");
	}
	
	public void StartClient()
	{
		if (_network != null)
			_network.disconnect();
		_network = new SerialClient(this);
		_network.connect("localhost");
	}
	
	//Update the whole map based on the received action messages
	private void UpdateMap(ArrayList<ActionMsg> msgs)
	{
		//all messages
		for(ActionMsg msg : msgs)
		{
			ArrayList<Territory> territories = msg.getActionMsg();
			
			//all territories of a message
			for(Territory territory : territories)
			{
				int tmp = territory.getId();
				_map._territories.get(tmp).Owner = territory.Owner;
				_map._territories.get(tmp).Units = territory.Units;
			}
		}
	}
	
	private void Attack(Integer friendly, Integer enemy)
	{
		
	}

	//Runnable interface's method
	@Override
	public void run()
	{
		Play();
	}
}