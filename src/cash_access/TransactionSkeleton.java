package cash_access;

import java.util.Arrays;

import mware_lib.ISkeleton;
import mware_lib.LogProxy;
import mware_lib.MethodCall;
import mware_lib.MethodReturn;

public class TransactionSkeleton implements ISkeleton {
	private final TransactionImplBase base;

	public TransactionSkeleton(TransactionImplBase base) {
		this.base = base;
	}

	@Override
	public MethodReturn call(MethodCall mc) {
		LogProxy.log(base.getClass(),
				base.getClass().getName() + ":" + mc.toString());
		String accountId = (String) mc.args[0];
		switch (mc.method) {
		case "deposit":
			System.out.println(Arrays.toString(mc.args));
			double amount1 = (double) mc.args[1];
			try {
				base.deposit(accountId, amount1);
			} catch (InvalidParamException e) {
				return new MethodReturn(e);
			} catch (Exception e) {
				return new MethodReturn(new InvalidParamException(
						"TransactionSkeleton:Account nicht gefunden"));
			}
			return new MethodReturn(null);
		case "withdraw":
			double amount2 = (double) mc.args[1];
			try {
				base.withdraw(accountId, amount2);
			} catch (InvalidParamException | OverdraftException e) {
				return new MethodReturn(e);
			} catch (Exception e) {
				return new MethodReturn(new InvalidParamException(
						"TransactionSkeleton:Account nicht gefunden"));
			}
			return new MethodReturn(null);
		case "getBalance":
			try {
				return new MethodReturn(base.getBalance(accountId));
			} catch (Exception e) {
				return new MethodReturn(new InvalidParamException(
						"TransactionSkeleton:Account nicht gefunden"));
			}
		}
		return new MethodReturn(new RuntimeException("Methode nicht gefunden: "
				+ mc.method));
	}
}
