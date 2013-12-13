package connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public abstract class Connection implements IConnection {

	Socket mySock;
	ObjectInputStream oin;
	OutputStream out;
	ObjectOutputStream oout;
	
	protected void setSocket(Socket s) throws IOException {
		mySock = s;
		out = mySock.getOutputStream();
		oout = new ObjectOutputStream(out);
		oin = new ObjectInputStream(mySock.getInputStream());
	}
	
	@Override
	public void send(Object o) throws IOException {
		oout.writeObject(o);
	}

	@Override
	public Object receive() throws IOException {
		Object ret = null;
		try {
			ret = oin.readObject();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e.getMessage());
		}
		return ret;
	}

	@Override
	public void close() {
		try {
			oin.close();
			out.close();
			mySock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
