package mware_lib;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class NameServiceLocal extends NameService {
	private final Map<String, Object> objects = new HashMap<String, Object>();
	private final IConnection conn;
	private final int obPort;
	
	private static final ConfigReader cr = ConfigReader
			.getConfigReader("middleware.config");
	private static final ILogger logger = Logger.getLogger(cr
			.read("LOG_METHOD"));

	public NameServiceLocal(IConnection conn, int obPort) {
		this.conn = conn;
		this.obPort = obPort;
	}

	@Override
	public void rebind(Object servant, String name) {
		logger.log("NameServiceLocal: rebind: " + name);
		if (!objects.containsKey(name)) {
			String host = "localhost";
			try {
				host = IPTools.getIP();
			} catch (SocketException e) {
				throw new RuntimeException(e.getMessage());
			}
			ObjectRef ref = new ObjectRef(name, host, obPort);
			try {
				conn.sendReceive("rebind;" + name + ";" + ref.stringRep());
			} catch (IOException e) {
				throw new RuntimeException("rebind nicht m�glich");
			}
		}

		objects.put(name, servant);
	}

	@Override
	public Object resolve(String name) {
		logger.log("NameServiceLocal: resolve: " + name);
		if (!objects.containsKey(name)) {
			try {
				Object mr = conn.sendReceive("resolve;" + name + ";");
				if (mr == null) {
					throw new RuntimeException("resolve(name) nicht m�glich");
				}
				objects.put(name, mr);
			} catch (IOException e) {
				throw new RuntimeException("resolve nicht m�glich");
			}
		}
		return objects.get(name);
	}
}
