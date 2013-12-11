package mware_lib;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import objects.MethodCall;
import objects.MethodReturn;

public abstract class IProxy {
	
	public MethodReturn call(MethodCall method) {
		MethodReturn ret = null;
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
			ret = mr;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}
}
