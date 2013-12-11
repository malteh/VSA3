package demo;

import bank_access.*;
import mware_lib.NameService;
import mware_lib.ObjectBroker;

public class ServerApplikation {

	public static void main(String[] args) {
		String host = "localhost";
		int port = 1234;
		ObjectBroker ob = ObjectBroker.init(host, port);
		NameService ns = ob.getNameService();
		Account konto = new Account();
		ns.rebind((Object) konto, konto.getId());
		ob.shutDown();
	}

}
