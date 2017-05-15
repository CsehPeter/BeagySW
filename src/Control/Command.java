package Control;

import java.io.Serializable;

import Network.NetMsg;

//Contains all the necessary fields to identify a player interaction's properties
public class Command extends NetMsg implements Serializable
{
	public CmdType Click;
	public Player Player;
	public int Units = -1;
	
	public int FromId = -1;
	public int ToId = -1;
	
	/**
	 * @param cmd Type of the player interaction
	 * @param player The player who's made the interaction
	 * @param units The number of units in the interaction
	 * @param fromId ID of the base territory from where the interaction start
	 * @param toId ID of the target territory
	 * @see Control.CmdType
	 * Used for attack, transfer
	 */
	public Command(CmdType cmd, Player player, int units, int fromId, int toId)
	{
		Click = cmd;
		this.Player = player;
		Units = units;
		FromId = fromId;
		ToId = toId;
	}
	
	/**
	 * @param cmd Type of the player interaction
	 * @param player The player who's made the interaction
	 * @see Control.CmdType
	 */
	public Command(CmdType cmd, Player player)
	{
		Click = cmd;
		this.Player = player;
	}
	
	/**
	 * @param cmd Type of the player interaction
	 * @param player The player who's made the interaction
	 * @param toId ID of the target territory
	 * @see Control.CmdType
	 * Used for deploy
	 */
	public Command(CmdType cmd, Player player, int toId)
	{
		Click = cmd;
		this.Player = player;
		ToId = toId;
	}
}
