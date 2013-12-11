package bank_access;

import mware_lib.ISkeleton;
import mware_lib.Id;
import objects.MethodCall;
import objects.MethodReturn;

public class Account extends AccountImplBase implements ISkeleton {
	
	private final String id;
	private double balance = 0;
	
	public Account() {
		super();
		id = Id.getNewId();
	}

	@Override
	public MethodReturn call(MethodCall mc) {
		switch (mc.method) {
		case "getBalance":
			return new MethodReturn(getBalance());
		case "transfer":
			double amount = (double) mc.args[0];
			try {
				transfer(amount);
			} catch (OverdraftException e) {
				return new MethodReturn(e);
			}
			return new MethodReturn(null);
		}
		return new MethodReturn(new RuntimeException("Methode nicht gefunden: " + mc.method));
	}

	@Override
	public void transfer(double amount) throws OverdraftException {
		if (balance + amount < 0) throw new OverdraftException("balance + amount < 0");
		balance += amount;		
	}

	@Override
	public double getBalance() {
		return balance;
	}

	@Override
	public String getId() {
		return id;
	}

}
