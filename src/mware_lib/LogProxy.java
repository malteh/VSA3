package mware_lib;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogProxy {

	private static Map<Class<?>, Logger> m = new HashMap<Class<?>, Logger>();

	public static void log(Class<?> c, String message) {
		Logger l = m.get(c);
		if (l == null) {
			l = Logger.getLogger(c.getSimpleName());

			FileHandler fh;
			try {
				fh = new FileHandler("log/" + c.getSimpleName() + ".log");
				l.addHandler(fh);
				SimpleFormatter formatter = new SimpleFormatter();
				fh.setFormatter(formatter);

			} catch (SecurityException | IOException e) {
				e.printStackTrace();
			}

			m.put(c, l);
		}
		if (l != null) {
			l.log(Level.INFO, message);
		}
	}
}
