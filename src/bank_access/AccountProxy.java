package bank_access;

import java.io.Serializable;

import mware_lib.IProxy;
import mware_lib.MethodCall;
import mware_lib.MethodReturn;
import mware_lib.ObjectBroker;
import mware_lib.ObjectRef;

public class AccountProxy extends AccountImplBase implements Serializable, IProxy {

	private static final long serialVersionUID = 727543035277413679L;

	private final ObjectRef ref;
	
	public AccountProxy(ObjectRef ref) {
		this.ref = ref;
	}

	@Override
	public void transfer(double amount) throws OverdraftException {
		MethodCall mc = new MethodCall(ref.id, "transfer", new Object[] {amount});
		MethodReturn mr = ObjectBroker.call(mc, getObjectRef());
		
		if (mr.exception != null && mr.exception instanceof OverdraftException) {
			throw (OverdraftException) mr.exception;
		} else if (mr.exception != null) {
			throw new RuntimeException(mr.exception.getMessage());
		}
	}

	@Override
	public double getBalance() {
		MethodCall mc = new MethodCall(ref.id, "getBalance", new Object[0]);
		MethodReturn mr = ObjectBroker.call(mc, getObjectRef());
		if (mr.exception != null) {
			throw (RuntimeException) mr.exception;
		}
		return (double) mr.value;
	}

	@Override
	public ObjectRef getObjectRef() {
		return ref;
	}
}
