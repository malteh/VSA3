package bank_access;

import mware_lib.ISkeleton;
import mware_lib.LogProxy;
import mware_lib.MethodCall;
import mware_lib.MethodReturn;

public class AccountSkeleton implements ISkeleton {
	private final AccountImplBase base;
	
	public AccountSkeleton(AccountImplBase base) {
		this.base = base;
	}

	@Override
	public MethodReturn call(MethodCall mc) {
		LogProxy.log(base.getClass(), base.getClass().getName() + ":" + mc.toString());;
		switch (mc.method) {
		case "getBalance":
			return new MethodReturn(base.getBalance());
		case "transfer":
			double amount = (double) mc.args[0];
			try {
				base.transfer(amount);
			} catch (Exception e) {
				return new MethodReturn(new OverdraftException(e.getMessage()));
			}
			return new MethodReturn(null);
		}
		return new MethodReturn(new RuntimeException("Methode nicht gefunden: "
				+ mc.method));
	}
}
