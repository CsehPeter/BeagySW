package Control;

import Gui.GUI;
import Network.Network;

public abstract class Game implements Runnable
{
	public GameState gameState; //a gui ezt állíthatja
	protected Player player;
	protected GUI gui;
	protected Map map;
	protected Network network;

	@Override
	public void run()
	{
		Play();
	}
	
	public void Play() 
	{
		while(true)
		{
			switch(gameState)
			{
				case Connected  : ConnectedState(); break;
				case Ready 		: ReadyState(); break;
				case Wait 		: WaitState(); break;
				case Deploy 	: DeployState(); break;
				case Attack 	: AttackState(); break;
				case Transfer 	: TransferState(); break;
				case Win 		: WinState(); return;
				case Defeat 	: DefeatState(); return;
				default: break;
			}
		}
	}

	public abstract void ConnectedState();
	public abstract void ReadyState();
	public abstract void WaitState();
	public abstract void DeployState();
	public abstract void AttackState();
	public abstract void TransferState();
	public abstract void WinState();
	public abstract void DefeatState();
}
