package bank_access;

import mware_lib.IProxy;
import mware_lib.ISkeleton;
import mware_lib.ObjectRef;

public abstract class AccountImplBase implements IProxy{
	public abstract void transfer(double amount) throws OverdraftException;

	public abstract double getBalance();

	public static AccountImplBase narrowCast(Object o) {
		return new AccountProxy((ObjectRef) o);
	}
	
	@Override
	public ISkeleton toSkeleton() {
		return new AccountSkeleton(this);
	}
}