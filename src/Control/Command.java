package Control;

import java.io.Serializable;

import Network.NetMsg;

public class Command extends NetMsg implements Serializable
{
	public Clicks Click;
	public Player Player;
	public int Units = -1;
	
	public int FromId = -1;
	public int ToId = -1;
	
	public Command(Clicks click, Player player, int units, int fromId, int toId)
	{
		Click = click;
		this.Player = player;
		Units = units;
		FromId = fromId;
		ToId = toId;
	}
	public Command(Clicks click, Player player)
	{
		Click = click;
		this.Player = player;
	}
	public Command(Clicks click, Player player, int toId)
	{
		Click = click;
		this.Player = player;
		ToId = toId;
	}
}
