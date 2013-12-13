package mware_lib;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import connection.ClientConnection;
import connection.IConnection;

public class NameServiceLocal extends NameService {

	private final Map<String, Object> objects;
	private final IConnection conn;
	
	public NameServiceLocal(String serviceName, int port) throws IOException {
		objects = new HashMap<String, Object>();
		conn = new ClientConnection(serviceName,port);
	}

	@Override
	public void rebind(Object servant, String name) {
		if (!objects.containsKey(name)) {
			MethodCall mc = new MethodCall(name, "rebind", new Object[] {servant, name});
			try {
				conn.send(mc);
			} catch (IOException e) {
				throw new RuntimeException("rebind nicht möglich");
			}
		}
		objects.put(name, servant);
	}

	@Override
	public Object resolve(String name) {
		if (!objects.containsKey(name)) {
			MethodCall mc = new MethodCall(name, "resolve", new Object[] {name});
			try {
				conn.send(mc);
				MethodReturn mr =  (MethodReturn) conn.receive();
				if (mr.exception != null || mr.value == null) {
					throw new RuntimeException("resolve(name) nicht möglich"); 
				}
				objects.put(name, mr.value);
			} catch (IOException e) {
				throw new RuntimeException("resolve nicht möglich");
			}
		}
		return objects.get(name);
	}
}
