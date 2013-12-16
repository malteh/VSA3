package connection;

import java.io.IOException;

public interface IConnection {
	public void send(Object o) throws IOException;
	public Object sendReceive(Object o) throws IOException;
	public String getLocalAddress();
}
