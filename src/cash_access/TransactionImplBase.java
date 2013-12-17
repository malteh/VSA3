package cash_access;

import mware_lib.ObjectRef;

public abstract class TransactionImplBase {
	public abstract void deposit(String accountId, double amount)
			throws InvalidParamException;

	public abstract void withdraw(String accountId, double amount)
			throws InvalidParamException, OverdraftException;

	public abstract double getBalance(String accountId)
			throws InvalidParamException;

	public static TransactionImplBase narrowCast(Object o) {
		return new TransactionProxy((ObjectRef) o);
	}
}