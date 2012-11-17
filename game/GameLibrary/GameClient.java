package GameLibrary;

import java.util.ArrayList;

import GameLibrary.util.Consts;

/*
 * were all the magic happens for the client
 * stores all the game values such as maps characters and more
 */

public class GameClient {

	private ArrayList<Character> characters = new ArrayList<Character>();
	private int id;
	private String name;
	public Map map = new Map();
	
	public GameClient(){
		id = Consts.DISCONNECTED;
		this.name = "";
	}
	
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
			if (unit.movement.x == Consts.MOVE_BACKWORD_RIGHT){
				float angleX = unit.object.rotation.x;
				float angleY = unit.object.rotation.y;
				float movement = (delta*(unit.speed * Consts.UNITSIZE));
				unit.object.position.x += ((float) Math.cos(Math.toRadians(angleY)) * movement); 
				unit.object.position.y -= ((float) Math.sin(Math.toRadians(angleX)) * movement); 
				unit.object.position.z -= ((float) Math.sin(Math.toRadians(angleY)) * movement);
			}
			if (unit.movement.x == Consts.MOVE_FORWORD_LEFT){
				float angleX = unit.object.rotation.x;
				float angleY = unit.object.rotation.y;
				float movement = (delta*(unit.speed * Consts.UNITSIZE));
				unit.object.position.x -= ((float) Math.cos(Math.toRadians(angleY)) * movement); 
				unit.object.position.y += ((float) Math.sin(Math.toRadians(angleX)) * movement);
				unit.object.position.z += ((float) Math.sin(Math.toRadians(angleY)) * movement);
			}
			if (unit.movement.z == Consts.MOVE_BACKWORD_RIGHT){
				float angleX = unit.object.rotation.x;
				float angleY = unit.object.rotation.y;
				float movement = (delta*(unit.speed * Consts.UNITSIZE));
				unit.object.position.x += ((float) Math.sin(Math.toRadians(angleY)) * movement); 
				unit.object.position.y -= ((float) Math.sin(Math.toRadians(angleX)) * movement); 
				unit.object.position.z += ((float) Math.cos(Math.toRadians(angleY)) * movement);
				
			}
			if (unit.movement.z == Consts.MOVE_FORWORD_LEFT) {
				float angleX = unit.object.rotation.x;
				float angleY = unit.object.rotation.y;
				float movement = (delta*(unit.speed * Consts.UNITSIZE));
				unit.object.position.x -= ((float) Math.sin(Math.toRadians(angleY)) * movement); 
				unit.object.position.y += ((float) Math.sin(Math.toRadians(angleX)) * movement);
				unit.object.position.z -= ((float) Math.cos(Math.toRadians(angleY)) * movement);
			}
			characters.set(i, unit);
		}
	} 
}
