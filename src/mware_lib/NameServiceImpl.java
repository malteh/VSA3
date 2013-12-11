package mware_lib;

import java.util.HashMap;
import java.util.Map;

public class NameServiceImpl extends NameService {

	private final Map<String, Object> objects;
	
	public NameServiceImpl() {
		objects = new HashMap<String, Object>();
	}

	@Override
	public void rebind(Object servant, String name) {
		objects.put(name, servant);
	}

	@Override
	public Object resolve(String name) {
		return objects.get(name);
	}
}
