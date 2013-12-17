package mware_lib;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class ObjectBrokerTask extends Thread {

	private static final ConfigReader cr = ConfigReader.getConfigReader("middleware.config");
	private static final ILogger logger = Logger.getLogger(cr.read("LOG_METHOD"));

	private int remainingClients = cr.readInt("MAX_CLIENTS");
	private final ObjectBroker ob;
	private final ServerSocket server;

	public ObjectBrokerTask(ObjectBroker ob) throws IOException {
		this.ob = ob;
		server = getNextFreeSocket();
	}

	private ServerSocket getNextFreeSocket() throws IOException {
		for (int i = 2000; i < 65000; i++) {
			try {
				ServerSocket s = new ServerSocket(i);
				return s;
			} catch (IOException e) {
				continue;
			}
		}

		throw new IOException("no free port found");
	}

	@Override
	public void run() {
		logger.log("OB port " + server.getLocalPort());
		ob.setPort(server.getLocalPort());
		while (!isInterrupted()) {
			if (remainingClients > 0) {
				try {
					Socket s = server.accept();
					ReceiverThread r = new ReceiverThread(s, this);
					r.start();
				} catch (IOException e) {
					logger.log(e.getMessage());
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

	public void increaseCounter() {
		remainingClients++;

	}

	public void decreaseCounter() {
		remainingClients--;
	}

	public class ReceiverThread extends Thread {
		private final ObjectBrokerTask nsg;
		private final Socket s;

		public ReceiverThread(Socket s, ObjectBrokerTask nsg) {
			this.s = s;
			this.nsg = nsg;
		}

		@Override
		public void run() {
			nsg.decreaseCounter();
			try {
				ObjectInputStream oin = new ObjectInputStream(
						s.getInputStream());
				MethodCall mc = (MethodCall) oin.readObject();
				logger.log(mc.toString());
				ISkeleton ip = ((IProxy) ob.getNameService().resolve(mc.id))
						.toSkeleton();
				MethodReturn mr = ip.call(mc);
				logger.log(mr.toString());
				OutputStream out = s.getOutputStream();
				ObjectOutputStream oout = new ObjectOutputStream(out);
				oout.writeObject(mr);
				out.close();
				oin.close();
				s.close();
			} catch (IOException | ClassNotFoundException e) {
				logger.log(e.getLocalizedMessage());
			}
			nsg.increaseCounter();
		}
	}
}