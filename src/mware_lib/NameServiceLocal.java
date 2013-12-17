package mware_lib;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import tools.IPTools;
import connection.IConnection;

public class NameServiceLocal extends NameService {
	private final Map<String, Object> objects = new HashMap<String, Object>();
	private final IConnection conn;
	private final int obPort;
	
	public NameServiceLocal(IConnection conn, int obPort) {
		this.conn = conn;
		this.obPort = obPort;
	}

	@Override
	public void rebind(Object servant, String name) {
		if (!objects.containsKey(name)) {
			String host = "localhost";
			try {
				host = IPTools.getIP();
			} catch (SocketException e) {
				throw new RuntimeException(e.getMessage());
			}
			ObjectRef ref = new ObjectRef(name, host, obPort);
			MethodCall mc = new MethodCall(name, "rebind", new Object[] {ref, name});
			try {
				conn.sendReceive(mc);
			} catch (IOException e) {
				throw new RuntimeException("rebind nicht m�glich");
			}
		}
		
		objects.put(name, servant);
	}

	@Override
	public Object resolve(String name) {
		if (!objects.containsKey(name)) {
			MethodCall mc = new MethodCall(name, "resolve", new Object[] {name});
			try {
				MethodReturn mr =  (MethodReturn) conn.sendReceive(mc);
				if (mr.exception != null || mr.value == null) {
					throw new RuntimeException("resolve(name) nicht m�glich"); 
				}
				objects.put(name, mr.value);
			} catch (IOException e) {
				throw new RuntimeException("resolve nicht m�glich");
			}
		}
		return objects.get(name);
	}
}
