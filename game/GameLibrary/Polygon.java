package GameLibrary;

import java.io.Serializable;
import java.util.ArrayList;
import org.lwjgl.util.vector.Vector3f;

public class Polygon implements Serializable{
	
	public ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
	public ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
	public ArrayList<Face> faces = new ArrayList<Face>();
	public Vector3f tranloc;
	
	public Polygon(float x, float y, float z){
		tranloc = new Vector3f(x,y,z);
	}
	
}
