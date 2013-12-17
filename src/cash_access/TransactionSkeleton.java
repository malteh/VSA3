package cash_access;

import java.util.Arrays;

import mware_lib.ConfigReader;
import mware_lib.ILogger;
import mware_lib.ISkeleton;
import mware_lib.Logger;
import mware_lib.MethodCall;
import mware_lib.MethodReturn;

public class TransactionSkeleton implements ISkeleton {
	private final TransactionImplBase base;

	private static final ConfigReader cr = ConfigReader
			.getConfigReader("middleware.config");
	private static final ILogger logger = Logger.getLogger(cr
			.read("LOG_METHOD"));

	public TransactionSkeleton(TransactionImplBase base) {
		this.base = base;
	}

	@Override
	public MethodReturn call(MethodCall mc) {
		logger.log(base.getClass().getName() + ":" + mc.toString());
		String accountId = (String) mc.args[0];
		switch (mc.method) {
		case "deposit":
			System.out.println(Arrays.toString(mc.args));
			double amount1 = (double) mc.args[1];
			try {
				base.deposit(accountId, amount1);
			} catch (InvalidParamException e) {
				return new MethodReturn(e);
			}
			return new MethodReturn(null);
		case "withdraw":
			double amount2 = (double) mc.args[1];
			try {
				base.withdraw(accountId, amount2);
			} catch (InvalidParamException | OverdraftException e) {
				return new MethodReturn(e);
			}
			return new MethodReturn(null);
		case "getBalance":
			try {
				return new MethodReturn(base.getBalance(accountId));
			} catch (InvalidParamException e) {
				return new MethodReturn(e);
			}
		}
		return new MethodReturn(new RuntimeException("Methode nicht gefunden: "
				+ mc.method));
	}
}
