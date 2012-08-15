package GameLibrary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

public class Loader {
	/* loads maps and blender objects into the program */
	
	public static Map readMap(String filename){
		float x,y,z;
		int r, g, b;
		Sector sector = null;
		Map map = new Map();
		try {
			String cwd = new java.io.File( "." ).getCanonicalPath();
			BufferedReader in = new BufferedReader(new FileReader(cwd + "/game/res/maps/" + filename + ".mp"));
		    String str;
		    while ((str = in.readLine()) != null) {
		    	map.mapstring.add(str);
		    	String[] list = str.split(" ");
		    	if (list[0].equals("S")){
		    		if (sector != null) {
		    			map.sectors.add(sector);
		    		}
		    		x = Float.valueOf(list[1]);
		    		y = Float.valueOf(list[2]);
		    		z = Float.valueOf(list[3]);
		    		sector = new Sector(x, y, z);
		    		
		    	} else if (list[0].equals("P")){
		    		x = Float.valueOf(list[2]);
		    		y = Float.valueOf(list[3]);
		    		z = Float.valueOf(list[4]);
		    		r = Integer.valueOf(list[5]);
		    		g = Integer.valueOf(list[6]);
		    		b = Integer.valueOf(list[7]);
		    		Polygon object = readObject(list[1], x, y, z);
		    		object.color.set(r, g, b);
		    		if (object != null){
		    			sector.objects.add(object);
		    		}
		    	} else if (list[0].equals("END")){
		    		map.sectors.add(sector);
		    	}
		    }
		    in.close();
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return map;
	}
	
	public static Map readMap(ArrayList<String> mapstring){
		float x,y,z;
		int r, g, b;
		Sector sector = null;
		Map map = new Map();
		map.mapstring = mapstring;
		
		for(String line : mapstring){
			String[] list = line.split(" ");
			if (list[0].equals("S")){
	    		if (sector != null) {
	    			map.sectors.add(sector);
	    		}
	    		x = Float.valueOf(list[1]);
	    		y = Float.valueOf(list[2]);
	    		z = Float.valueOf(list[3]);
	    		sector = new Sector(x, y, z);
	    		
	    	} else if (list[0].equals("P")){
	    		x = Float.valueOf(list[2]);
	    		y = Float.valueOf(list[3]);
	    		z = Float.valueOf(list[4]);
	    		r = Integer.valueOf(list[5]);
	    		g = Integer.valueOf(list[6]);
	    		b = Integer.valueOf(list[7]);
	    		Polygon object = readObject(list[1], x, y, z);
	    		object.color.set(r, g, b);
	    		if (object != null){
	    			sector.objects.add(object);
	    		}
	    	} else if (list[0].equals("END")){
	    		map.sectors.add(sector);
	    	}
		}
		return map;
	}
	
	
	public static Polygon readObject(String filename, float x, float y, float z){
		Polygon object = new Polygon(x, y, z);
	    
		try {
			String cwd = new java.io.File( "." ).getCanonicalPath();
			BufferedReader in = new BufferedReader(new FileReader(cwd + "/game/res/models/" + filename + ".obj"));
		    String str;
		    while ((str = in.readLine()) != null) {
		    	String[] list = str.split(" ");
		    	if (list[0].equals("v")){
		    		x = Float.valueOf(list[1]);
		    		y = Float.valueOf(list[2]);
		    		z = Float.valueOf(list[3]);
		    		object.vertices.add(new Vector3f(x,y,z));
		    	} else if (list[0].equals("vn")){
		    		x = Float.valueOf(list[1]);
		    		y = Float.valueOf(list[2]);
		    		z = Float.valueOf(list[3]);
		    		object.normals.add(new Vector3f(x,y,z));
		    	} else if (list[0].equals("f")){
		    		ArrayList<Integer> vertexIndices = new ArrayList<Integer>();
		    		ArrayList<Integer> normalIndices = new ArrayList<Integer>();
		    		for (int i = 1; i<list.length; i++){
		    			vertexIndices.add(Integer.valueOf(list[i].split("/")[0]));
		    			normalIndices.add(Integer.valueOf(list[i].split("/")[2]));
		    		}
		    		object.faces.add(new Face(vertexIndices, normalIndices));
		    	}
		    }  
		    in.close();
		}catch(Exception e){
			e.printStackTrace();
			Logger.log(Logger.ERROR, "failed to open file");
			return null;
		}
		return object;
	}
}
