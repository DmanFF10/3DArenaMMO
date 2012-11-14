package GameLibrary.util;

import java.io.BufferedWriter;
import java.io.FileWriter;


public class Logger {
	/* logs all the events to a file */
	
	// log levels
	public static final int ERROR = 0;
	public static final int DEBUG = 1;
	public static final int INFO = 2;
	public static final int WORNING = 3;
	
	// identifies if the is writable too
	private static boolean writable = true;
	private static String filename = "";

	public static void startLogger(String filename){
		// attempts to open the file
		try{
			// if successful write a entry
			Logger.filename = filename;
			BufferedWriter out = new BufferedWriter(new FileWriter(filename+".log"));
			System.out.println("[INFO] Started Logger");
			out.write("[INFO] Started Logger");
			out.newLine();
			out.close();
		}catch(Exception e){
			// if failed to open file 
			// don't alow any logs to be made
			writable = false;
		}
	}

	public static void log(int level, String message){
		// if writable add the log level to the message and
		// write out to the file
		if (writable){
			switch(level){
			case INFO:
				message = "[INFO] " + message;
				break;
			case ERROR:
				message = "[ERROR] " + message;
				break;
			case WORNING:
				message = "[WORNING] " + message;
				break;
			case DEBUG:
				message = "[DEBUG] " + message;
				break;
			}
			
			// write the message to the console
			System.out.println(message);

			// write the message to the file
			try{
				BufferedWriter out = new BufferedWriter(new FileWriter(Logger.filename + ".log", true));
				out.write(message);
				out.newLine();
				out.close();

			} catch(Exception e){
				// if writing to the file fails 
				// write to the console and disallow any more logs 
				writable = false;
				System.out.println("[ERROR] logger failed work");
			}
		}
	}

	public static boolean isActive(){
		// identifies if the logger is active
		return writable;
	}
}
