package mware_lib;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import log.ILogger;
import log.Logger;

import tools.ConfigReader;

import connection.Connection;
import connection.IConnection;

/**
 * * core of the middleware: * Maintains a Reference to the NameService *
 * Singleton
 */
public class ObjectBroker {

	private static ObjectBroker objectBroker = null;
	private static final ILogger logger = Logger.getLogger();

	private NameService nameService;
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

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		conn = new Connection(serviceName, port);
		this.nameService = new NameServiceLocal(conn, obPort);
	}

	public class ObjectBrokerTask extends Thread {

		private int remainingClients = ConfigReader.readInt("MAX_CLIENTS");
		private final ObjectBroker ob;
		private final ServerSocket server;

		public ObjectBrokerTask(ObjectBroker ob) throws IOException {
			this.ob = ob;
			server = getNextFreeSocket();
		}

		private ServerSocket getNextFreeSocket() throws IOException 
		{
			
			for(int i = 2000; i < 65000;i++) 
			{
				try 
				{
					ServerSocket s = new ServerSocket(i);
					return s;
				}
				catch (IOException e) 
				{
					continue;
				}
			}
			
			throw new IOException("no free port found");
		}
		
		@Override
		public void run() {
			logger.log("OB port "+server.getLocalPort());
			ob.setPort(server.getLocalPort());
			while (!isInterrupted()) {
				if (remainingClients > 0) {
					try {
						Socket s = server.accept();
						ObjectInputStream oin = new ObjectInputStream(
								s.getInputStream());
						MethodCall mc = (MethodCall) oin.readObject();
						logger.log(mc.toString());
						ISkeleton ip = ((IProxy) nameService.resolve(mc.id))
								.toSkeleton();
						MethodReturn mr = ip.call(mc);
						logger.log(mr.toString());
						OutputStream out = s.getOutputStream();
						ObjectOutputStream oout = new ObjectOutputStream(out);
						oout.writeObject(mr);
						out.close();
						oin.close();
						s.close();
						// ReceiverThread r = new ReceiverThread(conn,
						// this);
						// r.start();
					} catch (SocketException se) {
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		};

		public void shutDown() {
			try {
				this.interrupt();
				server.close();
			} catch (Exception e) {
			}
		}
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
			mr = (MethodReturn) c.sendReceive(method);
		} catch (IOException e) {
			mr = new MethodReturn(e);
		}
		return mr;
	}
}
