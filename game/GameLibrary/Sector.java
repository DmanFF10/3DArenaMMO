package GameLibrary;

import java.util.ArrayList;

public class Sector {

	private ArrayList<Polygon> objects = new ArrayList<Polygon>();
	
	public int size(){
		return objects.size();
	}
	
	public void addPolygon(Polygon obj){
		objects.add(obj);
	}
	
	public Polygon getPolygon(int index){
		return objects.get(index);
	}
	
}
