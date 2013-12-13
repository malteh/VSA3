package connection;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerConnection extends Connection {
	
	private final ServerSocket s;

	public ServerConnection(int port) throws IOException {
		s = new ServerSocket(port);
		s.accept();
	}
	
	@Override
	public Object receive() throws IOException {
		setSocket(s.accept());
		Object ret = null;
		try {
			ret = oin.readObject();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e.getMessage());
		}
		return ret;
	}
}
