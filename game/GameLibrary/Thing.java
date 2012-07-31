package GameLibrary;

import java.io.Serializable;

public class Thing implements Serializable {

	private int id, type;
	private String username;
	
	public Thing(int id, String username, int type){
		this.id = id;
		this.type = type;
		this.username = username;
	}
	
	public int getID(){
		return id;
	}
	
	public int getType(){
		return type;
	}
	
	public String getUsername(){
		return username;
	}
}
