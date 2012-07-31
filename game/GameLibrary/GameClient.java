package GameLibrary;

import java.util.ArrayList;

/*
 * were all the magic happens for the client
 * stores all the game values such as maps characters and more
 */

public class GameClient {

	private ArrayList<Character> characters = new ArrayList<Character>();
	private int id;
	private String name;
	public Map map;
	
	public GameClient(String name){
		id = Consts.DISCONNECTED;
		this.name = name;
	}
	
	public int getID(){
		return id;
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public Character getCharacter(int id){
		return characters.get(id);
	}
	
	public void setCharacter(int id, Character unit){
		characters.set(id, unit);
	}
	
	public void setCharacters(ArrayList<Character> characters){
		this.characters = characters;
	}
}
