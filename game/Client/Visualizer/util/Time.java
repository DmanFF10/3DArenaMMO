package Client.Visualizer.util;

import org.lwjgl.Sys;

public class Time {

	public static long getTime(){
		// return the current time
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	private static int getDelta(long lastFrame){
		// returns the time between frames (delta)
		long currentTime = getTime();
		int delta = (int) (currentTime - lastFrame);
		return delta;
	}
	
}
