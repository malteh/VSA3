package bank_access;

import mware_lib.ISkeleton;
import mware_lib.MethodCall;
import mware_lib.MethodReturn;

public class ManagerSkeleton implements ISkeleton {
	private final ManagerImplBase base;
	
	public ManagerSkeleton(ManagerImplBase base) {
		this.base = base;
	}

	@Override
	public MethodReturn call(MethodCall mc) {
		switch (mc.method) {
		case "createAccount":
			String owner = (String) mc.args[0];
			String branch = (String) mc.args[1];
			return new MethodReturn(base.createAccount(owner, branch));
		}
		return new MethodReturn(new RuntimeException("Methode nicht gefunden: "
				+ mc.method));
	}
}
