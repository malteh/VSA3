package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import objects.MethodCall;
import objects.MethodReturn;
import objects.Person;

public class Server {

	private Map<String, Object> objects = new HashMap<String, Object>();

	public static void main(String[] args) {
		Person p = new Person();
		Server s = new Server();
		s.objects.put("Peter", p);
		s.loop();
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

			Object o = this.objects.get(methodCall.id);
			Method m = o.getClass().getMethod(methodCall.method);
			Object antwort = m.invoke(o, methodCall.args);
			ret = new MethodReturn(antwort);
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
