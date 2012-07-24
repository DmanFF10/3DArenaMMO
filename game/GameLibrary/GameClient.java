package GameLibrary;

import java.util.ArrayList;

/*
 * were all the magic happens for the client
 * stores all the game values such as maps characters and more
 */

public class GameClient {

	private ArrayList<Character> characters = new ArrayList<Character>();
	private int id;
	public Map map;
	
	public int getID(){
		return id;
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public void setCharacters(ArrayList<Character> characters){
		this.characters = characters;
	}
	
	public Character getCharacter(int id){
		return characters.get(id);
	}
	
	public void setCharacter(int id, Character unit){
		characters.set(id, unit);
	}
	
}
