package bank_access;

import mware_lib.ObjectRef;

public abstract class ManagerImplBase {
	public abstract String createAccount(String owner, String branch);

	public static ManagerImplBase narrowCast(Object gor) {
		return new ManagerProxy((ObjectRef) gor);
	}
}