package bank_access;

import mware_lib.Id;
import mware_lib.MethodCall;
import mware_lib.MethodReturn;
import mware_lib.ObjectBroker;

public class AccountProxy extends AccountImplBase {

	private final String id;

	public AccountProxy() {
		super();
		id = Id.getNewId();
	}

	@Override
	public void transfer(double amount) throws OverdraftException {
		MethodCall mc = new MethodCall(id, "transfer", new Object[] {amount});
		MethodReturn mr = ObjectBroker.call(mc);
		
		if (mr.exception != null && mr.exception instanceof OverdraftException) {
			throw (OverdraftException) mr.exception;
		} else if (mr.exception instanceof RuntimeException)	{
			throw (RuntimeException) mr.exception;
		}
	}

	@Override
	public double getBalance() {
		MethodCall mc = new MethodCall(id, "getBalance", new Object[0]);
		MethodReturn mr = ObjectBroker.call(mc);
		if (mr.exception != null) {
			throw (RuntimeException) mr.exception;
		}
		return (double) mr.value;
	}

	@Override
	public String getId() {
		return id;
	}

}
