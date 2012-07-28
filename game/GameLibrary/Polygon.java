package GameLibrary;

import java.util.ArrayList;

public class Polygon {
	
	private ArrayList<Vertex> verts = new ArrayList<Vertex>();

	public int size(){
		return verts.size();
	}
	
	public void addVertex(Vertex vertex){
		verts.add(vertex);
	}
	
	public Vertex getVertex(int index){
		return verts.get(index);
	}
}
