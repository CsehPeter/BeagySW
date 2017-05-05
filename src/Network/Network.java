package Network;

import java.util.ArrayList;

import Control.Control;
import Network.Messages.*;

public abstract class Network
{
	public ArrayList<NetMsg> mailbox = new ArrayList<NetMsg>();
	
	//Return the StatusMsg which type is given in argument 
	public StatusMsg GetStatusMsg(StatusMsgType status, Boolean remove)
	{
		synchronized (mailbox)
		{
			for(NetMsg msg : mailbox)
			{
				if(msg instanceof StatusMsg)
				{
					if( ((StatusMsg) msg).getStatus() == status)
					{
						if(remove) mailbox.remove(msg);
						return (StatusMsg) msg;
					}
				}
			}
		}
		
		return null;
	}
	
	//Returns all ActionMsgs in the mailbox
	public ArrayList<ActionMsg> GetActionMsgs(Boolean remove)
	{
		ArrayList<ActionMsg> ret = new ArrayList<ActionMsg>();
		
		ArrayList<NetMsg> _mailbox = new ArrayList<NetMsg>();
		synchronized (mailbox)
		{
			_mailbox.addAll(mailbox);
		}
		
		for (NetMsg msg : _mailbox)
		{
			if (msg instanceof ActionMsg)
			{
				ret.add(new ActionMsg(msg.getPlayer(), ((ActionMsg)msg).GetActionMsg()) );
				if(remove) mailbox.remove(msg);
			}
		}
		
		return ret;
	}

	public abstract void connect(String ip);

	public abstract void disconnect();

	public abstract void send(NetMsg msg);
}