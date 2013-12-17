package globalns;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameServiceGlobal extends Thread implements INameServiceGlobal {

	private static final ConfigReader cr = ConfigReader
			.getConfigReader("global_nameservice.config");
	private static final int defaultPort = cr.readInt("DEFAULT_GLOBAL_NS_PORT");
	private static final ILogger logger = Logger.getLogger(cr
			.read("LOG_METHOD"));

	public static void main(String[] args) {
		Integer port = (args.length > 0) ? Integer.parseInt(args[0])
				: defaultPort;
		logger.log("NameServiceGlobal: gestartet auf Port " + port.toString()
				+ ", maximal " + cr.readInt("MAX_CLIENTS") + " Clients m�glich");
		NameServiceGlobal nsg = null;
		try {
			nsg = new NameServiceGlobal(port);
			nsg.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("GNS - ENTER zum beenden");
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
	private final Map<String, String> objects = new ConcurrentHashMap<String, String>();
	private int remainingClients = cr.readInt("MAX_CLIENTS");

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
		logger.log("NameServiceGlobal: beendet");
	}

	public String call(String mc) {
		Pattern p = Pattern.compile("^(.*);(.*);(.*)$");
		Matcher m = p.matcher(mc);
		if (m.matches()) {
			String method = m.group(1);
			String id = m.group(2);
			String arg = m.group(3);
			switch (method) {
			case "resolve":
				return objects.get(id);
			case "rebind":
				objects.put(id, arg);
			}
		}
		return null;
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
				String mc = (String) oin.readObject();
				logger.log("GNS - Eingehende Nachricht: " + mc);
				String mr = call(mc);
				logger.log("GNS - Ausgehende Nachricht: " + mr);
				OutputStream out = s.getOutputStream();
				ObjectOutputStream oout = new ObjectOutputStream(out);
				oout.writeObject(mr);
				out.close();
				oin.close();
				s.close();
			} catch (IOException | ClassNotFoundException e) {
				logger.log("GNS - Exception: " + e.getLocalizedMessage());
			}
			nsg.increaseCounter();
		}
	}
}
