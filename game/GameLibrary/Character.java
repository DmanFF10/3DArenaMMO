package GameLibrary;

import java.io.Serializable;

import org.lwjgl.util.vector.Vector3f;

/*
 *  Character locations and stats
 */

public class Character implements Serializable{
	private static final long serialVersionUID = 1L;
	public int speed = 100;
	public Vector3f movement = new Vector3f();
	public long timex = 0;
	public long timey = 0;
	public long timez = 0;
	public Polygon object;
	
	public Character(float x, float y, float z){
		object = Loader.readObject("test", x, y, z);
		object.color.setRed(100);
	}
	
	
	
}
