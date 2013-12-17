package mware_lib;

import java.net.*;
import java.util.Enumeration;

public class IPTools {
	public static String getIP() throws SocketException {
		String ret = "";
		Enumeration<?> e = NetworkInterface.getNetworkInterfaces();
		while (e.hasMoreElements()) {
			NetworkInterface n = (NetworkInterface) e.nextElement();
			Enumeration<?> ee = n.getInetAddresses();
			while (ee.hasMoreElements()) {
				InetAddress i = (InetAddress) ee.nextElement();
				if (!i.getHostAddress().startsWith("127")
						&& !i.getHostAddress().contains(":"))
					return i.getHostName();
				// if (!i.getHostAddress().startsWith("127") &&
				// !i.getHostAddress().contains(":"))
				// return i.getHostAddress();
			}
		}
		return ret;
	}
}
