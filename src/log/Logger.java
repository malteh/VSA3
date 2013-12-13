package log;

import tools.ConfigReader;

public class Logger {
	
	public static ILogger logger = null;
	
	private static void init() {
		String logMethod = ConfigReader.read("LOG_METHOD");
		switch (logMethod) {
		case "console":
			logger = new ConsoleLogger();
			break;
		default:
			break;
		}
	}
	
	public static ILogger getLogger() {
		if (logger == null) init();
		return logger;
	}
}
