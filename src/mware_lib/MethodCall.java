package mware_lib;

import java.io.Serializable;
import java.util.Arrays;

public class MethodCall implements Serializable {
	private static final long serialVersionUID = -6954108426305680689L;
	public String id, method;
	public Object[] args;
	public ObjectRef ref;

	public MethodCall(String id, String method, Object[] args) {
		this.id = id;
		this.method = method;
		this.args = args;
	}

	@Override
	public String toString() {
		return method + "(" + Arrays.toString(args) + ")";
	}
}
