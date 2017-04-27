package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import Control.Control;

public class SerialClient extends Network
{
	private Socket socket = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;

	public SerialClient(Control c)
	{
		super(c);
	}

	private class ReceiverThread implements Runnable {

		public void run()
		{
			System.out.println("Waiting for message...");
			try
			{
				while (true)
				{
					NetMsg msg = (NetMsg) in.readObject();
					if(msg != null) _mailbox.add(msg);
					
					//thread.sleep????
					Thread.sleep(100);
				}
			} 
			catch (Exception ex)
			{
				System.out.println(ex.getMessage());
				System.err.println("Server disconnected!");
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

	@Override
	public void send(NetMsg msg)
	{
		if (out == null) return;
		System.out.println("Sending point: " + msg + " to Server");
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
			if (out != null)
				out.close();
			if (in != null)
				in.close();
			if (socket != null)
				socket.close();
		}
		catch (IOException ex)
		{
			System.err.println("Error while closing conn.");
		}
	}
}