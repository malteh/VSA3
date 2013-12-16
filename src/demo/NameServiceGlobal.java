package demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import tools.ConfigReader;
import log.ILogger;
import log.Logger;
import mware_lib.ISkeleton;
import mware_lib.MethodCall;
import mware_lib.MethodReturn;

public class NameServiceGlobal extends Thread implements ISkeleton,
		INameServiceGlobal {

	private static final int defaultPort = ConfigReader
			.readInt("DEFAULT_GLOBAL_NS_PORT");
	private static final ILogger logger = Logger.getLogger();

	public static void main(String[] args) {
		Integer port = (args.length > 0) ? Integer.parseInt(args[0])
				: defaultPort;
		logger.log("NameServiceGlobal gestartet auf Port " + port.toString() + ", maximal " + ConfigReader.readInt("MAX_CLIENTS") + " Clients möglich");
		NameServiceGlobal nsg = null;
		try {
			nsg = new NameServiceGlobal(port);
			nsg.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("ENTER zum beenden");
		try {
			br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (nsg != null) {
			nsg.interrupt();
			nsg.close();
		}
	}

	private final ServerSocket s;
	private final Map<String, Object> objects = new ConcurrentHashMap<String, Object>();
	private int remainingClients = ConfigReader.readInt("MAX_CLIENTS");
	
	public NameServiceGlobal(int port) throws IOException {
		s = new ServerSocket(port);
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			if (remainingClients > 0) {
				try {
					Socket conn = s.accept();
					ReceiverThread r = new ReceiverThread(conn, this);
					r.start();
				} catch (SocketException se) {
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	};

	private void close() {
		try {
			s.close();
		} catch (Exception e) {
		}
		logger.log("NameServiceGlobal: Verbindung beendet");
	}

	public void rebind(Object servant, String name) {
		objects.put(name, servant);
	}

	public Object resolve(String name) {
		return objects.get(name);
	}

	@Override
	public MethodReturn call(MethodCall mc) {
		logger.log(mc.toString());
		try {
			switch (mc.method) {
			case "resolve":
				return new MethodReturn(objects.get(mc.id));
			case "rebind":
				objects.put(mc.id, mc.args[0]);
				return new MethodReturn(null);
			}
		} catch (Exception e) {
		}

		return new MethodReturn(new RuntimeException("Methode nicht gefunden: "
				+ mc.method));
	}

	@Override
	public void increaseCounter() {
		remainingClients++;

	}

	@Override
	public void decreaseCounter() {
		remainingClients--;
	}

	public class ReceiverThread extends Thread {
		private final Socket s;
		private final INameServiceGlobal nsg;

		public ReceiverThread(Socket s, INameServiceGlobal nsg) {
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
				MethodReturn mr = call(mc);
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
