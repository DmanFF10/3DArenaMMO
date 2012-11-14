package GameLibrary;


import java.io.Serializable;
import java.util.ArrayList;

import GameLibrary.Graphics.Sector;

/*
 * map holds the different sectors making up the map
 */

public class Map implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public ArrayList<String> mapstring = new ArrayList<String>(); 
	public ArrayList<Sector> sectors = new ArrayList<Sector>();
	
}

