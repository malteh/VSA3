package connection;

public interface IConnection {
	public void open(String host, int port);
	public Object sendReceive(Object o);
	public void close();
}
