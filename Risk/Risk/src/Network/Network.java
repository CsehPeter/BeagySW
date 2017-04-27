package Network;

import java.util.ArrayList;

import Control.Control;

public abstract class Network
{
	protected ArrayList<NetMsg> _mailbox = new ArrayList<NetMsg>();
	
	protected Control ctrl;

	Network(Control c)
	{
		ctrl = c;
	}
	
	public Boolean SearchMsg(StatusMsgType status)
	{
		for(NetMsg msg : _mailbox)
		{
			if(msg instanceof StatusMsg)
			{
				if( ((StatusMsg) msg).getStatus() == status)
				{
					_mailbox.remove(msg);
					return true;
				}
			}
		}
		return false;
	}
	
	public ArrayList<ActionMsg> GetActionMsgs()
	{
		ArrayList<ActionMsg> ret = new ArrayList<ActionMsg>();
		for(NetMsg msg : _mailbox)
		{
			if(msg instanceof ActionMsg)
			{
				ret.add((ActionMsg)msg);
				_mailbox.remove(msg);
			}
		}
		return ret;
	}

	public abstract void connect(String ip);

	public abstract void disconnect();

	public abstract void send(NetMsg msg);
}