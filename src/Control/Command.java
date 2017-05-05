package Control;

import java.io.Serializable;

import Network.NetMsg;

public class Command extends NetMsg implements Serializable
{
	public Clicks Click;
	public int Units = 0;
	
	public int FromId = 0;
	public int ToId = 0;
	
	public Command(Clicks click, int playerId, int units, int fromId, int toId)
	{
		Click = click;
		PlayerId = playerId;
		Units = units;
		FromId = fromId;
		ToId = toId;
	}
	public Command(Clicks click, int playerId)
	{
		Click = click;
		PlayerId = playerId;
	}
	public Command(Clicks click, int playerId, int toId)
	{
		Click = click;
		PlayerId = playerId;
		ToId = toId;
	}
}
