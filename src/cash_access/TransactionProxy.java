package cash_access;

import java.io.Serializable;
import mware_lib.IProxy;
import mware_lib.ISkeleton;
import mware_lib.MethodCall;
import mware_lib.MethodReturn;
import mware_lib.ObjectBroker;
import mware_lib.ObjectRef;

public class TransactionProxy extends TransactionImplBase implements Serializable, IProxy {

	private static final long serialVersionUID = 5267326855302113966L;
	
	private final ObjectRef ref;
	
	public TransactionProxy(ObjectRef ref) {
		this.ref = ref;
	}

	@Override
	public ObjectRef getObjectRef() {
		return ref;
	}
	
	@Override
	public ISkeleton toSkeleton() {
		return new TransactionSkeleton(this);
	}

	@Override
	public void deposit(String accountId, double amount)
			throws InvalidParamException {
		MethodCall mc = new MethodCall(ref.id, "deposit", new Object[] {accountId,amount});
		MethodReturn mr = ObjectBroker.call(mc, getObjectRef());
		if (mr == null || mr.exception != null) throw new InvalidParamException("TransactionProxy:Account nicht gefunden!");
	}

	@Override
	public void withdraw(String accountId, double amount)
			throws InvalidParamException, OverdraftException {
		MethodCall mc = new MethodCall(ref.id, "withdraw", new Object[] {accountId, amount});
		MethodReturn mr = ObjectBroker.call(mc, getObjectRef());
		if (mr.exception != null) {
			try {
				throw mr.exception;
			} catch (InvalidParamException | OverdraftException  e) {
				throw e;
			} catch (Exception e) {
				throw new RuntimeException(mr.exception.getMessage());
			}
		}
		if (mr == null || mr.exception != null) throw new InvalidParamException("TransactionProxy2:Account nicht gefunden!");
	}

	@Override
	public double getBalance(String accountId) throws InvalidParamException {
		MethodCall mc = new MethodCall(ref.id, "getBalance", new Object[] {accountId});
		MethodReturn mr = ObjectBroker.call(mc, getObjectRef());
		if (mr == null || mr.exception != null) throw new InvalidParamException("TransactionProxy:Account nicht gefunden!");
		return (double) mr.value;
	}

}
