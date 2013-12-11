package mware_lib;

/**
 * * core of the middleware: * Maintains a Reference to the NameService *
 * Singleton
 */
public class ObjectBroker {
	
	private static ObjectBroker objectBroker = null;
	private NameService nameService = new NameServiceImpl();
		
	private ObjectBroker(String serviceName, int port) {
		// TODO Auto-generated constructor stub
	}

	/** * @return an Implementation for a local NameService */
	public NameService getNameService() {
		return nameService;
	}

	/**
	 * * shuts down the process, the OjectBroker is running in * terminates
	 * process
	 */
	public void shutDown() { /* TODO */
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
}
