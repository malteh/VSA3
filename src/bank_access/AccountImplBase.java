package bank_access;

import mware_lib.ObjectRef;

public abstract class AccountImplBase {
	public abstract void transfer(double amount) throws OverdraftException;

	public abstract double getBalance();

	public static AccountImplBase narrowCast(Object o) {
		return new AccountProxy(ObjectRef.fromObjectRep(o));
	}
}