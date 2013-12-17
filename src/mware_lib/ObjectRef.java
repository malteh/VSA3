package mware_lib;

import java.io.Serializable;

public class ObjectRef implements Serializable {
	private static final long serialVersionUID = 9079593771909866121L;
	public final String id, host;
	public final int port;

	public ObjectRef(String id, String host, int port) {
		this.id = id;
		this.host = host;
		this.port = port;
	}

	@Override
	public String toString() {
		return "ObjectRef(ID:" + id + ", Host" + host + ", Port:" + port+")";
	}
}
