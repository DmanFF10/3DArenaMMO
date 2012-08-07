package GameLibrary;

import java.io.Serializable;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

public class Sector implements Serializable{

	private static final long serialVersionUID = 1L;
	public ArrayList<Polygon> objects = new ArrayList<Polygon>();
	public Vector3f position;
	
	public Sector(float x, float y, float z){
		position = new Vector3f(x,y,z);
	}
	
}
