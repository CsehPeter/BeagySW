package Network;

public abstract class Network
{
	public abstract void connect(String ip);

	public abstract void disconnect();

	public abstract void send(NetMsg msg);
}