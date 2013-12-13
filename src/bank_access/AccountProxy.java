package bank_access;

import java.io.Serializable;

import mware_lib.MethodCall;
import mware_lib.MethodReturn;
import mware_lib.ObjectBroker;

public class AccountProxy extends AccountImplBase implements Serializable {

	private static final long serialVersionUID = 727543035277413679L;

	private final String id;

	public AccountProxy(String id) {
		this.id = id;
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
