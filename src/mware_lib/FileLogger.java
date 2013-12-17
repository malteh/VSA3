package mware_lib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FileLogger implements ILogger {

	PrintWriter out = null;
	Calendar cal = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	OutputStreamWriter writer;

	@Override
	public void log(String message) {
		if (out == null) {
			try {
				File output = new File("mware.log");
				FileOutputStream fos = new FileOutputStream(output);
				writer = new OutputStreamWriter(fos);
			} catch (IOException e) {
				out.close();
				e.printStackTrace();
			}
		}
		
		try {
			writer.write(sdf.format(cal.getTime()) + " - " + message + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
