package bank_access;

import mware_lib.IProxy;
import mware_lib.ISkeleton;
import mware_lib.ObjectRef;

public abstract class ManagerImplBase implements IProxy {
	public abstract String createAccount(String owner, String branch);

	public static ManagerImplBase narrowCast(Object o) {
		return new ManagerProxy(ObjectRef.fromObjectRep(o));
	}
	
	@Override
	public ISkeleton toSkeleton() {
		return new ManagerSkeleton(this);
	}
}