package Network.Messages;

import java.io.Serializable;

import Control.Player;

public class StatusMsg extends NetMsg implements Serializable
{	
	private StatusMsgType _status;
	
	public StatusMsg(Player player, StatusMsgType status) throws NullPointerException
	{
		if(player == null) throw new NullPointerException("Player is null");
		_player = player;
		
		_status = status;
	}
	
	public StatusMsgType getStatus() 
	{
		return this._status;
	}
}
