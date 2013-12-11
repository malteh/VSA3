package objects;

import mware_lib.ISkeleton;

public class Person implements IPerson, ISkeleton {

	@Override
	public String getName() {
		return "Peter";
	}

	@Override
	public MethodReturn call(MethodCall mc) {
		
		switch (mc.method) {
		case "getName":
			return new MethodReturn(getName());
		}
		
		return new MethodReturn(new RuntimeException("Methode nicht gefunden: " + mc.method));
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return "0";
	}

}
