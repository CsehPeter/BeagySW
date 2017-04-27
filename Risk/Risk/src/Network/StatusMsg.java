package Network;

public class StatusMsg extends NetMsg
{
	private int _playerId;
	private StatusMsgType _status;
	
	public StatusMsg(int playerId, StatusMsgType status)
	{
		_playerId = playerId;
		_status = status;
	}
	
	public int getPlayerId() 
	{
		return this._playerId;
	}
	
	public StatusMsgType getStatus() 
	{
		return this._status;
	}
}
