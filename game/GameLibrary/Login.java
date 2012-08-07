package GameLibrary;

import java.io.Serializable;
import java.util.ArrayList;

public class Login extends Thing implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private ArrayList<Character> characters;
	private Map map;
	
	public Login(int id, String username){
		super(id, username, Consts.TYPE_LOGIN);
	}
	
	public Login(int id, String username, ArrayList<Character> characters, Map map){
		super(id, username, Consts.TYPE_LOGIN);
		this.characters = characters;
		this.map = map;
	}
	
	public Map getMap(){
		return map;
	}
	
	public ArrayList<Character> getCharacters(){
		return characters;
	}
}
