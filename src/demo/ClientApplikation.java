package demo;

import bank_access.AccountImplBase;
import bank_access.OverdraftException;
import mware_lib.NameService;
import mware_lib.ObjectBroker;

public class ClientApplikation {

	public static void main(String[] args) {
		String host = "localhost";
		int port = 1234;
		ObjectBroker ob = ObjectBroker.init(host, port);
		NameService ns = ob.getNameService();
		Object gor = ns.resolve("1");

		AccountImplBase account = AccountImplBase.narrowCast(gor);

		try {
			account.transfer(300.78);
		} catch (OverdraftException e) {
			e.printStackTrace();
		}

	}

}