package GameLibrary;

import java.util.ArrayList;

import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector3f;

public class Face {
	public ArrayList<Integer> vertex = new ArrayList<Integer>();
	public ArrayList<Integer> normal = new ArrayList<Integer>();
	public Face(ArrayList<Integer> vertex, ArrayList<Integer> normal){
		this.vertex = vertex;
		this.normal = normal;
	}
}
