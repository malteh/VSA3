package globalns;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigReader {

	private static Map<String, ConfigReader> crMap = new HashMap<String, ConfigReader>();

	private Properties prop = null;

	private ConfigReader(String filename) throws IOException {
		Reader r;
		r = new FileReader(new File(filename));
		prop = new Properties();
		prop.load(r);
		r.close();
		crMap.put(filename, this);
	}

	public String read(String key) {
		return prop.getProperty(key);
	}

	public int readInt(String key) {
		return Integer.parseInt(prop.getProperty(key));
	}

	public static ConfigReader getConfigReader(String filename) throws IOException {
		if (!crMap.containsKey(filename)) {
			crMap.put(filename, new ConfigReader(filename));
		}
		return crMap.get(filename);
	}
}
