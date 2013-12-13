package demo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import tools.ConfigReader;

import connection.IConnection;
import connection.ServerConnection;
import log.ILogger;
import log.Logger;
import mware_lib.ISkeleton;
import mware_lib.MethodCall;
import mware_lib.MethodReturn;
import mware_lib.NameService;

public class NameServiceGlobal extends NameService implements ISkeleton {

	private static final int defaultPort = ConfigReader.readInt("DEFAULT_GLOBAL_NS_PORT");
	private static final ILogger logger = Logger.getLogger();

	public static void main(String[] args) {
		Integer port = (args.length > 0) ? Integer.parseInt(args[0]) : defaultPort;
		logger.log("NameServiceGlobal gestartet auf Port " + port.toString());
		NameServiceGlobal nsg = null;
		try {
			nsg = new NameServiceGlobal(port);
			nsg.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void start() {
		while (true) {
			try {
				MethodCall mc = (MethodCall) conn.receive();
				logger.log(mc.toString());
				MethodReturn mr = call(mc);
				conn.send(mr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private final Map<String, Object> objects;
	private final IConnection conn;

	public NameServiceGlobal(int port) throws IOException {
		objects = new HashMap<String, Object>();
		conn = new ServerConnection(port);
	}

	@Override
	public void rebind(Object servant, String name) {
		objects.put(name, servant);
	}

	@Override
	public Object resolve(String name) {
		return objects.get(name);
	}

	@Override
	public MethodReturn call(MethodCall mc) {
		try {
			switch (mc.method) {
			case "resolve":
				return new MethodReturn(objects.get(mc.args[0]));
			case "rebind":
				objects.put((String) mc.args[1], mc.args[0]);
				return new MethodReturn(null);
			}
		} catch (Exception e) {
		}

		return new MethodReturn(new RuntimeException("Methode nicht gefunden: "
				+ mc.method));
	}
}
