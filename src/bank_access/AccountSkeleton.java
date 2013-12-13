package bank_access;

import mware_lib.ISkeleton;
import mware_lib.MethodCall;
import mware_lib.MethodReturn;

public class AccountSkeleton implements ISkeleton {
	private final AccountImplBase acc;
	
	public AccountSkeleton(AccountImplBase acc) {
		this.acc = acc;
	}

	@Override
	public MethodReturn call(MethodCall mc) {
		switch (mc.method) {
		case "getBalance":
			return new MethodReturn(acc.getBalance());
		case "transfer":
			double amount = (double) mc.args[0];
			try {
				acc.transfer(amount);
			} catch (OverdraftException e) {
				return new MethodReturn(e);
			} catch (RuntimeException e) {
				return new MethodReturn(e);
			}
			return new MethodReturn(null);
		}
		return new MethodReturn(new RuntimeException("Methode nicht gefunden: "
				+ mc.method));
	}
}