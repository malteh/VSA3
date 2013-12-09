package objects;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class PersonProxy implements IPerson {

	private final String id;

	public PersonProxy(String id) {
		this.id = id;
	}

	@Override
	public String getName() {
		MethodCall m = new MethodCall(this.id, getCurrentMethod(), new Object[0]);
		return (String) get(m);
	}

	public Object get(MethodCall method) {
		Object ret = "";
		Socket mySock;
		ObjectInputStream oin;
		OutputStream out;
		ObjectOutputStream oout;

		try {
			mySock = new Socket("localhost", 14001);

			out = mySock.getOutputStream();
			oout = new ObjectOutputStream(out);
			oin = new ObjectInputStream(mySock.getInputStream());

			oout.writeObject(method);

			MethodReturn mr = (MethodReturn) oin.readObject();

			oin.close();
			out.close();
			mySock.close();

			// !!! wird momentan noch abgefangen !!!
			if (mr.exception != null) {
				throw mr.exception;
			}
			ret = mr.value;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	private String getCurrentMethod() {
		// [0] is getStackTrace, [1] is getCurrentMethod, [2] is the calling Method
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}
}
