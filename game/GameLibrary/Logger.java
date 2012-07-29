package GameLibrary;

import java.io.BufferedWriter;
import java.io.FileWriter;


public class Logger {
	/* logs all the events to a file */
	public static final int ERROR = 0;
	public static final int DEBUG = 1;
	public static final int INFO = 2;
	public static final int WORNING = 3;
	
	public static void log(int level, String message){
		
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
		try{
			String cwd = new java.io.File( "." ).getCanonicalPath();
			BufferedWriter out = new BufferedWriter(new FileWriter("arena.log"));
			out.write(message);
			out.close();
		
		} catch(Exception e){
			System.out.println("ERROR: logger failed work");
		}
		
	}
	
	
	
}
