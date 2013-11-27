package cash_access;

public class OverdraftException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6243994450629054032L;

	public OverdraftException(String msg) {
		super(msg);
	}
}