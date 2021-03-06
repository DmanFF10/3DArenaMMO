package Client.Manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import GameLibrary.util.Logger;

public class Properties {
	
	public int width, 
				height,
				frameCap;
	public boolean fullscreen, 
					 debugMode;
	
	public Properties(){
		loadDefult();
		if (!loadProperties()){
			writeProperties();
		}
	}
	
	private void loadDefult(){
		width = 800;
		height = 600;
		frameCap = 60;
		fullscreen = false;
		debugMode = true;
	}
	
	private boolean loadProperties() {
		Logger.log(Logger.INFO, "Attempting to read properties");
		try {
			BufferedReader in = new BufferedReader(new FileReader("Properties.conf"));
		
			String str, key;
			String[] list;
		    while ((str = in.readLine()) != null) {
		    	list = str.split(" ");
		    	key = list[0];
		    	if (key.equals("width")){
		    		width = Integer.valueOf(list[1]);
		    	
		    	} else if(key.equals("height")){
		    		height = Integer.valueOf(list[1]);
		    	
		    	} else if(key.equals("frameCap")){
		    		frameCap = Integer.valueOf(list[1]);
		    	
		    	} else if (key.equals("fullscreen")){
		    		fullscreen = list[1].equals("true");
		    	
		    	} else if (key.equals("debugMode")){
		    		debugMode = list[1].equals("true");
		    	}
		    }
		    in.close();
		} catch (Exception e) {
			Logger.log(Logger.WORNING, "failed to load all of the properties. using defaults where needed");
			return false;
		}
		return true;
	}
	
	public void writeProperties() {
		Logger.log(Logger.INFO, "Writing properties file");
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("Properties.conf"));
			out.write("width " + width);
			out.newLine();
			out.write("height " + height);
			out.newLine();
			out.write("frameCap " + frameCap);
			out.newLine();
			out.write("fullscreen " + fullscreen);
			out.newLine();
			out.write("debugMode " + debugMode);
			out.close();
		}catch (Exception e) {
			Logger.log(Logger.ERROR, "failed to write properties list");
		}
	}
}