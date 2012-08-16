package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import GameLibrary.Logger;

public class Properties {
	
	public int width, height;
	
	public Properties(){
		loadDefult();
		if (!loadProperties()){
			createProperties();
		}
	}
	
	private void loadDefult(){
		width = 800;
		height = 600;
	}
	
	private boolean loadProperties() {
		Logger.log(Logger.INFO, "Atempting to read properties");
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
		    	}
		    }
		    in.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	private void createProperties() {
		Logger.log(Logger.INFO, "Creating properties file");
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("Properties.conf"));
			out.write("width " + width);
			out.newLine();
			out.write("height " + height);
			out.newLine();
			out.close();
		}catch (Exception e) {
			Logger.log(Logger.ERROR, "failed to write properties list");
		}
	}
}