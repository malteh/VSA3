package bank_access;

import mware_lib.IProxy;
import mware_lib.ISkeleton;

public abstract class AccountImplBase implements IProxy {
	public abstract void transfer(double amount) throws OverdraftException;

	public abstract double getBalance();

	public static AccountImplBase narrowCast(Object o) {
		return (AccountImplBase) o;
	}

	public abstract String getId();
	
	@Override
	public ISkeleton toSkeleton() {
		return new AccountSkeleton(this);
	}
}