package Tests;

import Network.*;
import Network.Messages.*;

import java.util.ArrayList;

import Control.*;

public final class NetworkTests
{
	private static Network sr;
	private static Network cl;
	
	public static void RunAll()
	{
		if(sr == null)	T_StartServer();
		if(cl == null) 	T_StartClient();
		T_SendMsgs();
	}
	public static void T_StartServer()
	{
		System.out.println("---Start server test---");
		//Control c = new Control();
		sr = new SerialServer();
		sr.connect("localhost");
	}
	public static void T_StartClient()
	{
		System.out.println("---Start client test---");
		//Control c = new Control();
		cl = new SerialClient();
		cl.connect("localhost");
	}

	public static void T_SendMsgs()
	{
		System.out.println("---Message sending tests---");
		
		Player p1 = new Player();
		Map map = new Map();
		ArrayList<Territory> ts = new ArrayList<Territory>();
		
		map.init();
		ts = map._territories;
		
		System.out.println("Server sending status message...");
		sr.send(new StatusMsg(p1, StatusMsgType.AllPlayerReady));
		
		while(cl.mailbox.isEmpty());
		System.out.println("Received message: " + cl.mailbox.get(cl.mailbox.size() - 1));
		System.out.println(cl.GetStatusMsg(StatusMsgType.AllPlayerReady, false));
		
		
		System.out.println("Client sending action message...");
		cl.send(new ActionMsg(p1, ts));
		 
		while(sr.mailbox.isEmpty());
		System.out.println("Received message: " + sr.mailbox.size() + "  " + sr.mailbox.get(sr.mailbox.size() - 1));
		for(ActionMsg amsg : sr.GetActionMsgs(true))
		{
			for(Territory t : amsg.GetActionMsg())
			{
				System.out.println(t.getId() + "  " + t.getName());
			}
		}
	}
}
