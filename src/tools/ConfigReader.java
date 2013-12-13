package tools;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Properties;

public class ConfigReader {
	
	private static String filename = "middleware.config";
	private static Properties prop = null;
	
	public static String read(String key) {
		if (prop == null) init();
		return prop.getProperty(key);
	}

	public static int readInt(String key) {
		if (prop == null) init();
		return Integer.parseInt(prop.getProperty(key));
	}
	
	public static void init() {
		Reader r;
		try {
			r = new FileReader(new File(filename));
			prop = new Properties();
			prop.load(r);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
