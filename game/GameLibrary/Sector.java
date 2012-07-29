package GameLibrary;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

public class Sector {

	public ArrayList<Polygon> objects = new ArrayList<Polygon>();
	public Vector3f tranloc;
	
	public Sector(float x, float y, float z){
		tranloc = new Vector3f(x,y,z);
	}
	
}
