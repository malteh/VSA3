package demo;

import mware_lib.IProxy;
import mware_lib.ObjectRef;
import bank_access.AccountImplBase;
import bank_access.OverdraftException;

public class Account extends AccountImplBase implements IProxy {

	private double balance = 0.0;
	
	@Override
	public void transfer(double amount) throws OverdraftException {
		if (balance + amount < 0) {
			throw new OverdraftException("balance + amount < 0");
		}
		balance += amount;
	}

	@Override
	public double getBalance() {
		return balance;
	}

	@Override
	public ObjectRef getObjectRef() {
		return null;
	}
}
