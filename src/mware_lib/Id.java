package mware_lib;

public class Id {

	private static Integer counter = 0;
	
	public static String getNewId() {
		return (counter++).toString();
	}
}
