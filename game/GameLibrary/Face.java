package GameLibrary;

import java.io.Serializable;
import java.util.ArrayList;

public class Face implements Serializable{
	public ArrayList<Integer> vertex = new ArrayList<Integer>();
	public ArrayList<Integer> normal = new ArrayList<Integer>();
	public Face(ArrayList<Integer> vertex, ArrayList<Integer> normal){
		this.vertex = vertex;
		this.normal = normal;
	}
	

}
