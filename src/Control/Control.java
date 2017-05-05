package Control;

import java.awt.Color;
import java.util.ArrayList;

import Gui.Colors;
import Gui.GUI;
import Network.*;
import Network.Messages.*;

//Wait for player to select network type, then start it's thread
public class Control
{
	private GameState _gameState;
	private Network _network;
	protected Player _player;
	private GUI _gui;
	
	//protected!!!
	protected Map _map = new Map();

	public Player GetPlayer()
	{
		return _player;
	}
	
	public void SetGui(GUI gui) throws NullPointerException
	{
		if(gui == null) throw new NullPointerException("gui is null");
		_gui = gui;
	}
	
	public Control()
	{
		_map = new Map();
		_player = new Player(0, Color.black);
	}

	//Start Network
	public void StartNetwork(NetworkType networkType)
	{
		if(networkType == NetworkType.Undefined) throw new IllegalArgumentException("Undefined network type");
		
		if (_network != null) _network.disconnect();
		
		if(networkType == NetworkType.Server)
		{
			_network = new SerialServer();
			_network.connect("localhost");
		}
		if(networkType == NetworkType.Client)
		{
			_network = new SerialClient();
			_network.connect("localhost");
		}
		
		_gameState = GameState.Connected;
	}
	
	//Send the ready signal
	public void SendReady()
	{
		_network.send(new StatusMsg(_player, StatusMsgType.Ready));
		_gameState = GameState.Ready;
	}	
	
	//Waiting for the player's turn
	private void Wait()
	{
		//Check if defeated
		if(_network.GetStatusMsg(StatusMsgType.Defeat, false) != null)
		{
			_gameState = GameState.Defeat;
		}
		
		//Update map
		ArrayList<ActionMsg> msgs = _network.GetActionMsgs(false);
		if(msgs.isEmpty() == false) //new msg
		{
			UpdateMap(msgs);
		}
		
		//Wait for turn started
		if(_network.GetStatusMsg(StatusMsgType.TurnStarted, false) != null)
		{
			_gameState = GameState.Deploy;
		}
	}

	//Update the whole map based on the received action messages
	private Map UpdateMap(ArrayList<ActionMsg> msgs)
	{
		//all messages
		for(ActionMsg msg : msgs)
		{
			ArrayList<Territory> territories = msg.GetActionMsg();
			
			//all territories of a message
			for(Territory territory : territories)
			{
				int tmp = territory.getId();
				_map._territories.get(tmp).Owner = territory.Owner;
				_map._territories.get(tmp).Units = territory.Units;
			}
		}
		return _map;
	}
}