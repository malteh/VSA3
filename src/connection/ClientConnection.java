package connection;

import java.io.IOException;
import java.net.Socket;

public class ClientConnection extends Connection {
	public ClientConnection(String host, int port) throws IOException {
		setSocket(new Socket(host, port));
	}
}
