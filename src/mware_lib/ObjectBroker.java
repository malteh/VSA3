package mware_lib;

import java.io.IOException;

/**
 * * core of the middleware: * Maintains a Reference to the NameService *
 * Singleton
 */
public class ObjectBroker {

	private static ObjectBroker objectBroker = null;

	private final NameService nameService;
	private IConnection conn;
	private int obPort;
	private final ObjectBrokerTask t;

	private ObjectBroker(String serviceName, int port) {

		try {
			t = new ObjectBrokerTask(this);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		t.start();

		while (obPort == 0)
			try {
				Thread.sleep(50);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		conn = new Connection(serviceName, port);
		this.nameService = new NameServiceLocal(conn, obPort);
		LogProxy.log(this.getClass(), String.format("ObjectBroker auf Port %s gestartet", obPort));
	}

	/** * @return an Implementation for a local NameService */
	public NameService getNameService() {
		return nameService;
	}

	public void setPort(int localPort) {
		this.obPort = localPort;
	}

	/**
	 * * shuts down the process, the OjectBroker is running in * terminates
	 * process
	 */
	public void shutDown() {
		t.shutDown();
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

	public static MethodReturn call(MethodCall method, ObjectRef ref) {
		IConnection c = new Connection(ref.host, ref.port);
		MethodReturn mr;
		try {
			LogProxy.log(ObjectBroker.class, String.format("ObjectBroker: %s an %s gesendet", method,
							ref));
			mr = (MethodReturn) c.sendReceive(method);
			LogProxy.log(ObjectBroker.class, String.format("ObjectBroker: %s von %s empfangen",
									mr, ref));
		} catch (IOException e) {
			mr = new MethodReturn(e);
		}
		return mr;
	}
}
