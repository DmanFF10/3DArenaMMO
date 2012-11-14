package GameLibrary.Graphics;

import java.io.Serializable;
import java.util.ArrayList;

public class Face implements Serializable{
	private static final long serialVersionUID = 1L;
	public ArrayList<Integer> vertex = new ArrayList<Integer>();
	public ArrayList<Integer> normal = new ArrayList<Integer>();
	public Face(ArrayList<Integer> vertex, ArrayList<Integer> normal){
		this.vertex = vertex;
		this.normal = normal;
	}
	

}
