package demo;

import java.io.IOException;

import mware_lib.MethodCall;
import mware_lib.MethodReturn;
import connection.Connection;
import connection.IConnection;

public class DemoNSOpponent {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IConnection c = new Connection("localhost", 22334);
		MethodCall m = new MethodCall("1", "rebind", new Object[] { "asd" });
		try {
			c.send(m);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		m = new MethodCall("1", "resolve");
		try {
			System.out.println(((MethodReturn)c.sendReceive(m)).value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
