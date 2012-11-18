package GameLibrary;

public class Player {

	int id;
	String username;
	
	public Player(int id, String name){
		this.id = id;
		this.username = name;
	}
	
	public int getID(){
		return id;
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public String getUsername(){
		return username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
}
