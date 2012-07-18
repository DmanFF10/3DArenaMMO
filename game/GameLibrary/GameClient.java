package GameLibrary;

import java.util.ArrayList;

/*
 * were all the magic happens for the client
 * stores all the game values such as maps characters and more
 */

public class GameClient {

	private ArrayList<Character> characters = new ArrayList<Character>();
	public Map map;
	
	public GameClient(){ }
	
	public void setMap(Map map){
		this.map = map;
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
