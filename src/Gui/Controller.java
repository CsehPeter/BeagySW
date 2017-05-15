package Gui;

import java.awt.Color;
import java.awt.Graphics2D;

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
	
	public int GetPlayerId() throws NullPointerException
	{
		if(_player == null) throw new NullPointerException("Player is null");
		return _player.getId();
	}
	
	Controller(GUI gui, Map map) throws NullPointerException
	{
		if(gui == null) throw new NullPointerException("gui is null");
		_gui = gui;
		
		if(map == null) throw new NullPointerException("map is null");
		_map = map;
	}
	
	public void StartServer()
	{
		ctrl = new Logic(this, _map);
		SerialServer sr = new SerialServer(ctrl);
		
		((Logic)ctrl).AddGame(sr); //Ez igen ronda
		
		sr.connect("localhost");
		
		_player = new Player(0, Color.blue);
	}
	public void StartClient()
	{
		ctrl = new SerialClient(this);
		((SerialClient)ctrl).connect("localhost");
		
		_player = new Player(1, Color.red);
		
		ctrl.OnCommand(new Command(CmdType.ClientConnected, _player));
	}
	public void Exit()
	{
		ctrl.OnCommand(new Command(CmdType.Exit, _player));
	}

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
	
	public void BtnNext()
	{
		ctrl.OnCommand(new Command(CmdType.Next, _player));
		
		//TODO check this:
		ActivateTerritory(_active[0], 0, Color.black);
		ActivateTerritory(_active[1], 1, Color.black);
		_active[0] = -1;
		_active[1] = -1;
	}
	
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
	
	private void ActivateTerritory(int territoryId, int idx, Color color)
	{
		if(territoryId < 1 || territoryId > Constants.NUMBER_OF_TERRITORIES) return;
		
		//TODO remove debug print
		//System.out.println("Act0: " + _active[0] + "  Act1: " + _active[1] + "  Terr: " + territoryId);
		
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
	
	public void Test1()
	{
		_gui.ClearLog();
		//SupportTests.RunAll();
	}
	public void Test2()
	{
		//System.out.println("Running Test2");
		//ctrl.OnCommand(new Command(Clicks.Ok, _player.getId(), 30, 1, 2));
		
		_gui.AppendLog("blablablablabla");
		
		//Tests.SupportTests.T_Battle();
	}

	@Override
	public void OnGameState(GameState gs)
	{		
		_gs = new GameState(gs.Phase, gs.ChangedTerritories, gs.Player);
		
		
		for(Territory t : gs.ChangedTerritories )
		{
			
			//System.out.println(t.getId() + "  " + t.Units);
			
			_map.getTerritory(t.getId()).Owner = t.Owner; //new Player(t.Owner.getId(), t.Owner.getColor());
			_map.getTerritory(t.getId()).Units = t.Units;
			
			_gui.PaintTerritory(t.getId(), t.Owner.getColor());
		}

		_gui.UpdateStatus(gs.Player.getId(), gs.Phase);
		
//		_gui.AppendLog(gs.Phase.toString());
		
		String logMessage = "";

		switch(_gs.Phase){
			case Deploy:
				logMessage = "P"+gs.Player.getId()+" deploy";
				break;
			case Attack:
				if(gs.ChangedTerritories.size() == 2){
					logMessage = "P"+gs.Player.getId()+
						" attacked \nfrom: "+gs.ChangedTerritories.get(0).getName()+
						"\nto: "+ gs.ChangedTerritories.get(1).getName();
				}
//				else logMessage = "attack";
				break;
			case Transfer:
				logMessage = "P"+gs.Player.getId()+
				" transfered \nfrom: "+gs.ChangedTerritories.get(0).getName()+
				"\nto: "+ gs.ChangedTerritories.get(1).getName();
				break;
			default:
				logMessage = "default";
				break;
		}
		_gui.AppendLog(logMessage); // ez elrontja a terkepet
	}
}
