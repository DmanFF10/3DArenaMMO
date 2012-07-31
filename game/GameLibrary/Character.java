package GameLibrary;

import java.io.Serializable;

/*
 *  Character locations and stats
 */

public class Character implements Serializable{

	public float X;
	public float Y;
	public float Z;
	public int speed = 100;
	public Polygon object;
	// TODO: add texture stuff
	
	public Character(float x, float y, float z){
		X = x; Y = y; Z = z;
		object = Loader.readObject("test", x, y, z);
	}
	
	
	
}
