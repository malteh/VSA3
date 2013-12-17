package globalns;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FileLogger implements ILogger {

	PrintWriter out = null;
	Calendar cal = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	@Override
	public void log(String message) {
		if (out == null)
			try {
				out = new PrintWriter(new BufferedWriter(new FileWriter(
						"mware.log", true)));
				out.println(sdf.format(cal.getTime()) + " - " + message);
			} catch (IOException e) {
				out.close();
				e.printStackTrace();
			}

	}

}
