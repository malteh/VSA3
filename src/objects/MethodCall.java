package objects;

import java.io.Serializable;

public class MethodCall implements Serializable {
	private static final long serialVersionUID = -6954108426305680689L;
	public String id,method;
	public Object[] args;
	
	public MethodCall(String id, String method, Object[] args) 
	{
		this.id=id;
		this.method=method;
		this.args=args;
	}

}
