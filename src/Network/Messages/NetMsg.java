package Network.Messages;

import java.io.Serializable;

import Control.*;

public abstract class NetMsg implements Serializable
{
	protected Player _player;
	public Player getPlayer()
	{
		return _player;
	}
}