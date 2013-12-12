package mware_lib;

import connection.Connection;
import connection.IConnection;

/**
 * * core of the middleware: * Maintains a Reference to the NameService *
 * Singleton
 */
public class ObjectBroker {
	
	private static ObjectBroker objectBroker = null;
	private NameService nameService;
	private static final IConnection conn = new Connection();
	
	private ObjectBroker(String serviceName, int port) {
		this.nameService = new NameServiceImpl();
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
		
	}

	/**
	 * * Initializes the ObjectBroker / creates the local NameService * @param
	 * serviceName * hostname or IP of Nameservice * @param port * port
	 * NameService is listening at * @return an ObjectBroker Interface to
	 * Nameservice
	 */
	public static ObjectBroker init(String serviceName, int port) {
		conn.open(serviceName, port);
		if (objectBroker == null) {
			objectBroker = new ObjectBroker(serviceName, port);
		}
		return objectBroker;
	}
	

	public static MethodReturn call(MethodCall method) {
		MethodReturn mr = (MethodReturn) conn.sendReceive(method);
		return mr;
	}
}
