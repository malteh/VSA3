package connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Connection implements IConnection {

	Socket mySock;
	ObjectInputStream oin;
	OutputStream out;
	ObjectOutputStream oout;

	@Override
	public void open(String host, int port) {

		try {
			mySock = new Socket(host, port);
			out = mySock.getOutputStream();
			oout = new ObjectOutputStream(out);
			oin = new ObjectInputStream(mySock.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object sendReceive(Object o) {
		Object ret = null;

		try {
			oout.writeObject(o);
			ret = oin.readObject();
		} catch (Exception e) {
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
