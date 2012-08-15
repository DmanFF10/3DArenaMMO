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
	public Map map = new Map();
	
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
	
	public void addCharacter(Character unit){
		characters.add(unit);
	}
	
	public int playerSize(){
		return characters.size();
	}
	
	public void updateUnits(int delta){
		for(int i=0; i<playerSize(); i++){
			Character unit = getCharacter(i);
			if (unit.movement.z == Consts.MOVE_BACKWORD_RIGHT){
				float angle = unit.object.rotation.y;
				float movement = (delta*(unit.speed * Consts.UNITSIZE));
				unit.object.position.x += (float) Math.sin(Math.toRadians(angle)) * movement;
				unit.object.position.z += (float) Math.cos(Math.toRadians(angle)) * movement;
			}

			if (unit.movement.z == Consts.MOVE_FORWORD_LEFT) {
				float angle = unit.object.rotation.y;
				float movement = (delta*(unit.speed * Consts.UNITSIZE));
				unit.object.position.x -= (float) Math.sin(Math.toRadians(angle)) * movement;
				unit.object.position.z -= (float) Math.cos(Math.toRadians(angle)) * movement;
			}

			if (unit.movement.x == Consts.MOVE_FORWORD_LEFT){
				float angle = unit.object.rotation.y;
				float movement = (delta*(unit.speed * Consts.UNITSIZE));
				unit.object.position.z -= (float) Math.sin(Math.toRadians(angle)) * movement;
				unit.object.position.x -= (float) Math.cos(Math.toRadians(angle)) * movement;
			}

			if (unit.movement.x == Consts.MOVE_BACKWORD_RIGHT){
				float angle = unit.object.rotation.y;
				float movement = (delta*(unit.speed * Consts.UNITSIZE));
				unit.object.position.z += (float) Math.sin(Math.toRadians(angle)) * movement;
				unit.object.position.x += (float) Math.cos(Math.toRadians(angle)) * movement;
			}
			characters.set(i, unit);
		}
	} 
}
