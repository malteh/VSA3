package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import mware_lib.ISkeleton;
import mware_lib.MethodCall;
import mware_lib.MethodReturn;

public class Server {

	private Map<String, ISkeleton> objects = new HashMap<String, ISkeleton>();

	public static void main(String[] args) {
		try {
			ServerSocket s = new ServerSocket(0);
			s.accept();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		ISkeleton acc = new AccountProxy();
//		
//		Server s = new Server();
//		s.objects.put(acc.getId(), acc);
//		s.loop();
	}

	public void loop() {
		ServerSocket mySvrSocket;
		ObjectInputStream oin;
		ObjectOutputStream oout;
		Socket mySock;
		try {
			mySvrSocket = new ServerSocket(14001);
			mySock = mySvrSocket.accept();

			oin = new ObjectInputStream(mySock.getInputStream());
			oout = new ObjectOutputStream(mySock.getOutputStream());

			// LOOP here >
			doIt(oin, oout);
			// <

			oin.close();
			oout.close();
			mySock.close();

			mySvrSocket.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void doIt(ObjectInputStream oin, ObjectOutputStream oout) {
		MethodReturn ret = null;
		MethodCall methodCall;

		try {
			methodCall = (MethodCall) oin.readObject();

			ISkeleton o = this.objects.get(methodCall.id);
			System.out.println(o.getClass().getName().toString() + "." + methodCall.method);
			ret = o.call(methodCall);
		} catch (Exception e) {
			ret = new MethodReturn(e);
		}

		try {
			oout.writeObject(ret);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
