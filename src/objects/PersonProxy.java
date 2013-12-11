package objects;

import mware_lib.IProxy;

public class PersonProxy extends IProxy implements IPerson {

	private final String id;

	public PersonProxy(String id) {
		this.id = id;
	}

	@Override
	public String getName() {
		MethodCall m = new MethodCall(this.id, "getName", new Object[0]);
		return (String) call(m).value;
	}
}
