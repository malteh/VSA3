package connection;

import java.io.IOException;

public interface IConnection {
	public void send(Object o) throws IOException;
	public Object receive() throws IOException;
	public void close();
}
