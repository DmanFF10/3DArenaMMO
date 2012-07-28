package GameLibrary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/*
 * map holds the different sectors making up the map
 */

public class Map {
	
	private ArrayList<Sector> sectors = new ArrayList<Sector>();
	
	public int size(){
		return sectors.size();
	}
	
	public void addSector(Sector sector){
		sectors.add(sector);
	}
	
	public Sector getSector(int index){
		return sectors.get(index);
	}
	
	public boolean readFromFile(){
		
	    int x, y, z;
	    Sector sector = new Sector();
	    Polygon object = new Polygon();
	    Vertex vertex = new Vertex();
		
		try {
		    BufferedReader in = new BufferedReader(new FileReader("/home/gauge/test.mp"));
		    String str;
		    
		    while ((str = in.readLine()) != null) {
		    	String[] list = str.split(" ");
		    	
		    	// p identifies the end of an object
		    	if (list[0].equals("p")){
		    		// adds the object to the sector and
		    		// resets the object to nothing
		    		sector.addPolygon(object);
		    		object = new Polygon();
		    	
		        // s identifies the end of a sector
		    	} else if(list[0].equals("s")){
		    		// adds the sector to the map and
		    		// sets the sector to nothing
		    		addSector(sector);
		    		sector = new Sector();
		    	
		        // if it is not a letter they will be three numbers
		    	// for the x,y and z positions
		    	} else {
			    	vertex.X = Integer.parseInt(list[0]);
			    	vertex.Y = Integer.parseInt(list[1]);
			    	vertex.Z = Integer.parseInt(list[2]);
			    	object.addVertex(vertex);
			    	vertex = new Vertex();
		    	}
		    	
		    }
		    
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
