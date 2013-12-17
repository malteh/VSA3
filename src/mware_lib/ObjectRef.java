package mware_lib;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObjectRef implements Serializable {
	private static final long serialVersionUID = 9079593771909866121L;
	public final String id, host;
	public final int port;

	public ObjectRef(String id, String host, int port) {
		this.id = id;
		this.host = host;
		this.port = port;
	}

	public String stringRep() {
		return String.format("ObjectRef(%s,%s,%s)", id, host, port);
	}

	public static ObjectRef fromObjectRep(Object rep) {
		String srep = (String) rep;
		Pattern p = Pattern.compile("ObjectRef\\((.*),(.*),(\\d*)\\)");
		Matcher m = p.matcher(srep);
		if (m.matches()) {

			return new ObjectRef(m.group(1), m.group(2), Integer.parseInt(m
					.group(3)));
		} else {
			throw new RuntimeException();
		}
	}
	@Override
	public String toString() {
		return "ObjectRef(ID:" + id + ", Host:" + host + ", Port:" + port + ")";
	}
}
