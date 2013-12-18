package local_test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import bank_access.*;
import mware_lib.NameService;
import mware_lib.ObjectBroker;

public class ServerApplikation {

	public static void main(String[] args) {
		String host = "localhost";
		int port = 22334;
		ObjectBroker ob = ObjectBroker.init(host, port);
		NameService ns = ob.getNameService();
		AccountImplBase konto = new Account();
		ns.rebind((Object) konto, "1");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("ENTER zum beenden");
		try {
			br.readLine();
		} catch (Exception e) {
		}
		
		ob.shutDown();
	}
}
