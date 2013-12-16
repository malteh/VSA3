package mware_lib;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
			ObjectRef ref = new ObjectRef(name, conn.getLocalAddress(), obPort);
			MethodCall mc = new MethodCall(name, "rebind", new Object[] {ref, name});
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
				MethodReturn mr =  (MethodReturn) conn.sendReceive(mc);
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
