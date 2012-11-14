package GameLibrary.Graphics;

import java.io.Serializable;
import java.util.ArrayList;

import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector3f;


public class Polygon implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
	public ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
	public ArrayList<Face> faces = new ArrayList<Face>();
	public Vector3f position = new Vector3f(); 
	public Vector3f rotation = new Vector3f();
	public Color color = new Color();
	
	public Polygon(float x, float y, float z){
		position = new Vector3f(x,y,z);
	}
	
	public Polygon(float x, float y, float z, float rx, float ry, float rz){
		position = new Vector3f(x,y,z);
		rotation = new Vector3f(rx,ry,rz);
	}
}
