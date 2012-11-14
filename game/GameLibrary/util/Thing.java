package GameLibrary.util;

import java.io.Serializable;
import java.util.Date;

public class Thing implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int id, type;
	private String username;
	private long time;
	
	public Thing(int id, String username, int type){
		this.id = id;
		this.type = type;
		this.username = username;
		this.time = new Date().getTime();
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
	
	public long getTimeStamp(){
		return time;
	}
}
