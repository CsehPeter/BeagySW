package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import Control.Command;
import Control.GameState;
import Control.ICommand;
import Control.IGameState;

public class SerialServer extends Network implements IGameState
{
	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;
	
	private ICommand _cmdIf;
	
	public SerialServer(ICommand cmdIf) throws NullPointerException
	{
		if(cmdIf == null) throw new NullPointerException("cmdIf is null");
		_cmdIf = cmdIf;
	}
	
	@Override
	public void OnGameState(GameState gs) throws NullPointerException
	{
		if(gs == null) throw new NullPointerException("gs is null");
		send(gs);
	}

	private class ReceiverThread implements Runnable
	{

		public void run()
		{
			try
			{
				System.out.println("Waiting for Client");
				clientSocket = serverSocket.accept();
				System.out.println("Client connected.");
			}
			catch (IOException e)
			{
				System.err.println("Accept failed.");
				disconnect();
				return;
			}

			try
			{
				out = new ObjectOutputStream(clientSocket.getOutputStream());
				in = new ObjectInputStream(clientSocket.getInputStream());
				out.flush();
			}
			catch (IOException e)
			{
				System.err.println("Error while getting streams.");
				disconnect();
				return;
			}
			
			try
			{
				while (true)
				{
					NetMsg msg = (NetMsg) in.readObject();
					if(msg instanceof Command)
					{
						_cmdIf.OnCommand((Command) msg);
					}
					
					//thread.sleep????
					Thread.sleep(5);
				}
			}
			catch (Exception ex)
			{
				System.out.println(ex.getMessage());
				System.err.println("Client disconnected!");
			}
			finally
			{
				disconnect();
			}
		}
	}

	@Override
	public void connect(String ip)
	{
		disconnect();
		try
		{
			serverSocket = new ServerSocket(10007);

			Thread rec = new Thread(new ReceiverThread());
			rec.start();
		}
		catch (IOException e)
		{
			System.err.println("Could not listen on port: 10007.");
		}
	}

	@Override
	public void send(NetMsg msg)
	{
		if (out == null) return;
		//System.out.println("Sending message: " + msg + " to Client");
		try
		{
			out.writeObject(msg);
			out.flush();
		}
		catch (IOException ex)
		{
			System.err.println("Send error.");
		}
	}

	@Override
	public void disconnect()
	{
		try
		{
			if (out != null) out.close();
			if (in != null) in.close();
			if (clientSocket != null) clientSocket.close();
			if (serverSocket != null) serverSocket.close();
		}
		catch (IOException ex)
		{
			Logger.getLogger(SerialServer.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	
}