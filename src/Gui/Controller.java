package Gui;

import java.awt.Color;

import Control.CmdType;
import Control.Command;
import Control.Constants;
import Control.GameState;
import Control.ICommand;
import Control.IGameState;
import Control.Logic;
import Control.Phases;
import Control.Player;
import Control.Support;
import Network.SerialClient;
import Network.SerialServer;

//Middle layer between the graphical and the logic part
public class Controller implements IGameState
{
	private ICommand ctrl;
	private GameState _gs;
	private Player _player;
	private Map _map;
	
	private GUI _gui;
	
	//Territory selection
	private int[] _active = {-1, -1};
	private int _idx = 0;
	
	/**
	 * @return ID of the player in the Controller object
	 * @throws NullPointerException Thrown when the player is null reference
	 */
	public int GetPlayerId() throws NullPointerException
	{
		if(_player == null) throw new NullPointerException("Player is null");
		return _player.getId();
	}
	
	/**
	 * @param gui Reference to the GUI which instantiate this class
	 * @param map Reference to the map of the GUI
	 * @throws NullPointerException Thrown when any parameter is null
	 * @see Gui.GUI
	 * @see Gui.Map
	 */
	Controller(GUI gui, Map map) throws NullPointerException
	{
		if(gui == null) throw new NullPointerException("gui is null");
		_gui = gui;
		
		if(map == null) throw new NullPointerException("map is null");
		_map = map;
	}
	
	//Starts a Server on local host
	public void StartServer()
	{
		ctrl = new Logic(this, _map);
		SerialServer sr = new SerialServer(ctrl);
		
		((Logic)ctrl).AddGame(sr); //Ez igen ronda
		
		sr.connect("localhost");
		
		_player = new Player(0, Color.blue);
	}
	
	//Start a Client and connects to the server
	public void StartClient()
	{
		ctrl = new SerialClient(this);
		((SerialClient)ctrl).connect("localhost");
		
		_player = new Player(1, Color.red);
		
		ctrl.OnCommand(new Command(CmdType.ClientConnected, _player));
	}
	
	//Called from the Exit button
	public void Exit()
	{
		ctrl.OnCommand(new Command(CmdType.Exit, _player));
	}

	//Called from the OK button
	public void BtnOk()
	{
		if(_gs.Player.getId() == _player.getId() && (_gs.Phase == Phases.Attack || _gs.Phase == Phases.Transfer))
		{
			//TODO Change the number of attacking units
			ctrl.OnCommand(new Command(CmdType.Ok, _player, _map.getTerritory(_active[0]).Units - 1, _active[0], _active[1]));
			
			//TODO check this
			ActivateTerritory(_active[0], 0, Color.black);
			ActivateTerritory(_active[1], 1, Color.black);
			_active[0] = -1;
			_active[1] = -1;
		}
		
	}
	
	//Called from the Next button
	public void BtnNext()
	{
		ctrl.OnCommand(new Command(CmdType.Next, _player));
		
		//TODO check this:
		ActivateTerritory(_active[0], 0, Color.black);
		ActivateTerritory(_active[1], 1, Color.black);
		_active[0] = -1;
		_active[1] = -1;
	}
	
	/**
	 * @param territoryId ID of the territory which the player clicked on
	 */
	public void ClickOnMap(int territoryId)
	{
		if(_gs.Player.getId() == _player.getId())
		{
			if(_gs.Phase == Phases.Deploy)
			{
				ctrl.OnCommand(new Command(CmdType.Territory, _player, 1, -1, territoryId));
			}
			
			if(_gs.Phase == Phases.Attack)
			{
				if(Support.IsFriendlyTerritory(_player.getId(), _map.getTerritory(territoryId)))
				{
					ActivateTerritory(territoryId, 0, Constants.ACTIVE_COLOR[0]);
				}
				else
				{
					ActivateTerritory(territoryId, 1, Constants.ACTIVE_COLOR[1]);
				}
			}
			
			if(_gs.Phase == Phases.Transfer)
			{
				if(Support.IsFriendlyTerritory(_player.getId(), _map.getTerritory(territoryId)) == false) return;
				
				ActivateTerritory(territoryId, _idx, Constants.ACTIVE_COLOR[_idx]);
				if(_idx == 0)
					_idx = 1;
				else
					_idx = 0;
			}
		}
	}
	
	/**
	 * @param territoryId ID of the territory which should be activated
	 * @param idx Determines which active territory, from the array of the two active territories
	 * @param color Paint colour of the active territory
	 */
	private void ActivateTerritory(int territoryId, int idx, Color color)
	{
		if(territoryId < 1 || territoryId > Constants.NUMBER_OF_TERRITORIES) return;
		
		if(_active[idx] == territoryId)
		{
			_active[idx] = -1;
			_gui.PaintTerritory(territoryId, _map.getTerritory(territoryId).Owner.getColor());
		}
		else
		{
			if(_active[idx] == -1)
			{
				_active[idx] = territoryId;
				_gui.PaintTerritory(territoryId, color);
			}
			else
			{
				_gui.PaintTerritory(_active[idx], _map.getTerritory(territoryId).Owner.getColor());
				_active[idx] = territoryId;
				_gui.PaintTerritory(territoryId, color);
			}
		}
	}
	
	//Called from T1 button
	public void Test1()
	{
		_gui.ClearLog();
		//SupportTests.RunAll();
	}
	
	//Called from T2 button
	public void Test2()
	{
		_gui.AppendLog("blablablablabla");
		
		//Tests.SupportTests.T_Battle();
	}

	/**
	 * @param gs The changed game state
	 * @see Control.GameState
	 */
	@Override
	public void OnGameState(GameState gs)
	{		
		_gs = new GameState(gs.Phase, gs.ChangedTerritories, gs.Player);
		
		
		for(Territory t : gs.ChangedTerritories )
		{
			_map.getTerritory(t.getId()).Owner = t.Owner; 
			_map.getTerritory(t.getId()).Units = t.Units;
			
			_gui.PaintTerritory(t.getId(), t.Owner.getColor());
		}

		_gui.UpdateStatus(gs.Player.getId(), gs.Phase);
		
		String logMessage = "";

		switch(_gs.Phase){
			case Deploy:
				logMessage = "P"+gs.Player.getId()+" deploy to: "+gs.ChangedTerritories.get(0).getName();
				break;
			case Attack:
				if(gs.ChangedTerritories.size() == 2){
					logMessage = "P"+gs.Player.getId()+
						" attacked \nfrom: "+gs.ChangedTerritories.get(1).getName()+
						"\nto: "+ gs.ChangedTerritories.get(0).getName();
				}
				break;
			case Transfer:
				if(gs.ChangedTerritories.size() == 2){
					logMessage = "P"+gs.Player.getId()+
						" transfered \nfrom: "+gs.ChangedTerritories.get(0).getName()+
						"\nto: "+ gs.ChangedTerritories.get(1).getName();
				}
				break;
			default:
				break;
		}
		_gui.AppendLog(logMessage);
	}
}
