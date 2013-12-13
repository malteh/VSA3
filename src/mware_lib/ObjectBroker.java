package mware_lib;

import java.io.IOException;

import connection.ClientConnection;
import connection.IConnection;

/**
 * * core of the middleware: * Maintains a Reference to the NameService *
 * Singleton
 */
public class ObjectBroker {
	
	private static ObjectBroker objectBroker = null;

	private NameService nameService;

	private ObjectBroker(String serviceName, int port) {
		try {
			this.nameService = new NameServiceLocal(serviceName, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** * @return an Implementation for a local NameService */
	public NameService getNameService() {
		return nameService;
	}

	/**
	 * * shuts down the process, the OjectBroker is running in * terminates
	 * process
	 */
	public void shutDown() {
		//conn.close();
	}

	/**
	 * * Initializes the ObjectBroker / creates the local NameService * @param
	 * serviceName * hostname or IP of Nameservice * @param port * port
	 * NameService is listening at * @return an ObjectBroker Interface to
	 * Nameservice
	 */
	public static ObjectBroker init(String serviceName, int port) {
		if (objectBroker == null) {
			objectBroker = new ObjectBroker(serviceName, port);
		}
		return objectBroker;
	}

	public static MethodReturn call(MethodCall method) {
		IConnection conn = new ClientConnection(host, port);
		MethodReturn mr;
		try {
			conn.send(method);
			mr = (MethodReturn) conn.receive();
		} catch (IOException e) {
			mr = new MethodReturn(e);
		}
		return mr;
	}
}
