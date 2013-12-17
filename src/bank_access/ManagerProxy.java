package bank_access;

import java.io.Serializable;
import mware_lib.IProxy;
import mware_lib.ISkeleton;
import mware_lib.MethodCall;
import mware_lib.MethodReturn;
import mware_lib.ObjectBroker;
import mware_lib.ObjectRef;

public class ManagerProxy extends ManagerImplBase implements Serializable, IProxy {

	private static final long serialVersionUID = -8610148128344175195L;
	
	private final ObjectRef ref;
	
	public ManagerProxy(ObjectRef ref) {
		this.ref = ref;
	}

	@Override
	public ObjectRef getObjectRef() {
		return ref;
	}
	
	@Override
	public ISkeleton toSkeleton() {
		return new ManagerSkeleton(this);
	}

	@Override
	public String createAccount(String owner, String branch) {
		MethodCall mc = new MethodCall(ref.id, "createAccount", new Object[] {owner,branch});
		MethodReturn mr = ObjectBroker.call(mc, getObjectRef());
		if (mr.exception != null) {
			throw new RuntimeException(mr.exception.getMessage());
		}
		return (String) mr.value;
	}
}
