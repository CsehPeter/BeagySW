package Control;

import java.io.Serializable;

import Network.NetMsg;

public class Command extends NetMsg implements Serializable
{
	public CmdType Click;
	public Player Player;
	public int Units = -1;
	
	public int FromId = -1;
	public int ToId = -1;
	
	public Command(CmdType click, Player player, int units, int fromId, int toId)
	{
		Click = click;
		this.Player = player;
		Units = units;
		FromId = fromId;
		ToId = toId;
	}
	public Command(CmdType click, Player player)
	{
		Click = click;
		this.Player = player;
	}
	public Command(CmdType click, Player player, int toId)
	{
		Click = click;
		this.Player = player;
		ToId = toId;
	}
}
