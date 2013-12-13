package mware_lib;

public class ObjectRef {
	public final String id, host;
	public final int port;
	
	public ObjectRef(String id,String host,int port) {
		this.id = id;
		this.host = host;
		this.port = port;
	}
}
