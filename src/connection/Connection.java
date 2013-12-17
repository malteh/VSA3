package connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection implements IConnection {

	private String host;
	private int port;

	public Connection(String host, int port) {
		this.host = host;
		this.port = port;
	}

	@Override
	public Object sendReceive(Object o) throws IOException {
		Object ret = null;
		try {
			Socket s = new Socket(host, port);
			OutputStream out = s.getOutputStream();
			ObjectOutputStream oout = new ObjectOutputStream(out);
			oout.writeObject(o);
			ObjectInputStream oin = new ObjectInputStream(s.getInputStream());
			ret = oin.readObject();
			oin.close();
			out.close();
			s.close();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e.getMessage());
		}
		return ret;
	}

	@Override
	public String getLocalAddress() {
		ServerSocket s;
		String addr = "";
		try {
			s = new ServerSocket(0);
			addr = s.getInetAddress().getHostAddress();
			s.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return addr;
	}
}
