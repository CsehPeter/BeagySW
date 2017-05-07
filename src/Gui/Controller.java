package Gui;

import java.awt.Color;

import Control.Clicks;
import Control.Command;
import Control.GameState;
import Control.ICommand;
import Control.IGameState;
import Control.Logic;
import Control.Player;
import Network.SerialClient;
import Network.SerialServer;

public class Controller implements IGameState
{
	private ICommand ctrl;
	private GameState _gs;
	private Player _player;
	private Map _map;
	
	private GUI _gui;
	
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
		ctrl = new Logic(this);
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
		
		ctrl.OnCommand(new Command(Clicks.ClientConnected, _player.getId()));
	}
	public void Exit()
	{
		ctrl.OnCommand(new Command(Clicks.Exit, _player.getId()));
	}

	public void BtnOk()
	{
		ctrl.OnCommand(new Command(Clicks.Ok, _player.getId()));
	}
	
	public void BtnNext()
	{
		ctrl.OnCommand(new Command(Clicks.Next, _player.getId()));
	}
	
	public void ClickOnMap(int territoryId)
	{
		_gui.PaintTerritory(territoryId, Color.pink);
//		t.setFillColor(player.getColor());
//		country =t.getId() + " " + t.getName() + " - " + e.getX() + ", " + e.getY() ;
//		
//		if(_gs.PlayerId == player.getId() && _gs.Phase == Phases.Deploy)
//		{
//			cmd = new Command(Clicks.Territory, player.getId(), -1, -1, t.getId());
//			ctrl.OnCommand(cmd);
//		}
//		//TODO belekontárkodtam
//		mapPanel.RePaintTerritory(t.getId(), Color.cyan);
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
	}

	@Override
	public void OnGameState(GameState gs)
	{
		_gs = new GameState(gs.Phase, gs.ChangedTerritories, gs.PlayerId);
		
		for(Control.Territory t : gs.ChangedTerritories )
		{
			_gui.PaintTerritory(t.getId(), Color.red);
		}

		_gui.UpdateStatus(gs.PlayerId, gs.Phase);
	}
}
