package demo;

import bank_access.*;
import mware_lib.NameService;
import mware_lib.ObjectBroker;

public class ServerApplikation {

	public static void main(String[] args) {
		String host = "localhost";
		int port = 22334;
		ObjectBroker ob = ObjectBroker.init(host, port);
		NameService ns = ob.getNameService();
		AccountProxy konto = new AccountProxy();
		ns.rebind((Object) konto, "1");
		ob.shutDown();
	}
}
