package mware_lib;

public class Logger {
	
	public static ILogger logger = null;
	
	private static void init(String logMethod) {
		switch (logMethod) {
		case "console":
			logger = new ConsoleLogger();
			break;
		case "none":
			logger = new ILogger() {
				@Override
				public void log(String message) {
					// do nothing
				}
			};
			break;
		default:
			break;
		}
	}
	
	public static ILogger getLogger(String logMethod) {
		if (logger == null) init(logMethod);
		return logger;
	}
}
