package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import Control.Command;
import Control.GameState;
import Control.ICommand;
import Control.IGameState;


//Client in the network
public class SerialClient extends Network implements ICommand
{
	private Socket socket = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;
	
	private IGameState _game;
	
	/**
	 * @param game Game on which this object can call the OnGameState method
	 */
	public SerialClient(IGameState game)
	{
		if(game == null) throw new NullPointerException("game is null");
		_game = game;
	}
	
	/**
	 * @param cmd Command which should be send to the server
	 * @see Control.Command
	 */
	@Override
	public void OnCommand(Command cmd)
	{
		send(cmd);
	}

	//Thread which read the messages
	private class ReceiverThread implements Runnable
	{

		public void run()
		{
			System.out.println("Waiting for message...");
			try
			{
				while (true)
				{
					NetMsg msg = (NetMsg) in.readObject();
					
					if(msg instanceof GameState)
					{
						//TODO remove debug print
						//System.out.println("Client changedTerritories size: " + ((GameState)msg).ChangedTerritories.size());
						
						_game.OnGameState((GameState)msg);
					}
					Thread.sleep(5);
				}
			} 
			catch (Exception ex)
			{
				System.out.println(ex.getMessage() + ex.toString());
				System.err.println("Server disconnected!");
			}
			finally
			{
				disconnect();
			}
		}
	}

	/**
	 * @param Client connect to this ip address
	 */
	@Override
	public void connect(String ip)
	{
		disconnect();
		try
		{
			socket = new Socket(ip, 10007);

			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			out.flush();

			Thread rec = new Thread(new ReceiverThread());
			rec.start();
		}
		catch (UnknownHostException e)
		{
			System.err.println("Don't know about host");
		}
		catch (IOException e)
		{
			System.err.println("Couldn't get I/O for the connection. ");
			JOptionPane.showMessageDialog(null, "Cannot connect to server!");
		}
	}

	/**
	 * @param msg Message which should be sent
	 */
	@Override
	public void send(NetMsg msg) throws NullPointerException
	{
		if(msg == null) throw new NullPointerException("msg is null");
		if (out == null) throw new NullPointerException("out is null");;
		
		//System.out.println("Sending message: " + msg + " to Server");
		
		try
		{
			out.writeObject(msg);
			out.flush();
			out.reset();
		}
		catch (IOException ex)
		{
			System.err.println("Send error in client.");
		}
	}

	//Disconnects the client from the server
	@Override
	public void disconnect()
	{
		try
		{
			if (out != null) out.close();
			if (in != null) in.close();
			if (socket != null) socket.close();
		}
		catch (IOException ex)
		{
			System.err.println("Error while closing conn.");
		}
	}
}