package mware_lib;

import objects.MethodCall;
import objects.MethodReturn;

public interface ISkeleton {
	public MethodReturn call(MethodCall method);
	public String getId();
}
