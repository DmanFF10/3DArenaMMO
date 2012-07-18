package GameLibrary;

import java.util.ArrayList;

/*
 * were all the magic happens for the client
 * stores all the game values such as maps characters and more
 */

public class GameServer {
	
	private ArrayList<Character> characters = new ArrayList<Character>();
	public Map map;
	
	public GameServer(){
		map = new Map(500, 500, 500);
	}
	
	public ArrayList<Character> getCharacters(){
		return characters;
	}
	
	public Character getCharacter(int id){
		return characters.get(id);
	}
	
	public void setCharacter(int id, Character unit){
		characters.set(id, unit);
	}
	
}
