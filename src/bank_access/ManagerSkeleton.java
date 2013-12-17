package bank_access;

import mware_lib.ConfigReader;
import mware_lib.ILogger;
import mware_lib.ISkeleton;
import mware_lib.Logger;
import mware_lib.MethodCall;
import mware_lib.MethodReturn;

public class ManagerSkeleton implements ISkeleton {
	private final ManagerImplBase base;
	
	private static final ConfigReader cr = ConfigReader
			.getConfigReader("middleware.config");
	private static final ILogger logger = Logger.getLogger(cr
			.read("LOG_METHOD"));
	
	public ManagerSkeleton(ManagerImplBase base) {
		this.base = base;
	}

	@Override
	public MethodReturn call(MethodCall mc) {
		logger.log(base.getClass().getName() + ":" + mc.toString());
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
