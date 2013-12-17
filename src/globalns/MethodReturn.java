package globalns;

import java.io.Serializable;

public class MethodReturn implements Serializable {

	private static final long serialVersionUID = 5131104326536867270L;
	public final Exception exception;
	public final Object value;
	
	public MethodReturn(Exception exception) {
		this.exception = exception;
		this.value = null;
	}
	
	public MethodReturn(Object value) {
		this.exception = null;
		this.value = value;
	}
	
	@Override
	public String toString() {
		if (value != null) {
			return value.toString();
		}
		if (exception != null) {
			return exception.getMessage();
		}
		
		return "void";
	}
}
