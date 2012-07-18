package GameLibrary;

import java.util.ArrayList;

/*
 * map information such as height, width, depth and object 
 */

public class Map {
	
	private int height, width, depth;
	private ArrayList<Object> objects = new ArrayList<Object>();
	
	public Map(int height, int width, int depth){
		this.height = height;
		this.width = width;
		this.depth = depth;
	}
	
	public int height(){
		return height;
	}
	
	public int width(){
		return width;
	}
	
	public int depth(){
		return depth;
	}
	
	//TODO: add object details

}
