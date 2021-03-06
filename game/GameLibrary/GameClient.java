package GameLibrary;

import java.util.ArrayList;


/*
 * were all the magic happens for the client
 * stores all the game values such as maps characters and more
 */

public class GameClient {

	private ArrayList<Player> players = new ArrayList<Player>();
	private ArrayList<String[]> chat =new ArrayList<String[]>();
	private int id;
	public Map map = new Map();
	
	public GameClient(){
		id = Consts.DISCONNECTED;
	}
	
	public int getID(){
		return id;
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public String getName(){
		if (id != Consts.DISCONNECTED){
			return players.get(id).getUsername();
		}
		return "";
	}
	
	public void setName(String name){
		if (id != Consts.DISCONNECTED){
			players.get(id).setUsername(name);
		}
	}
	
	public void addPlayer(int id, String username){
		while(players.size() <= id){
			players.add(null);
		}
		players.set(id ,new Player(id, username));
	}
	
	public void removePlayer(int id){
		players.set(id, null);
	}
	
	public Player getPlayer(int id){
		return players.get(id);
	}
	
	public ArrayList<String> getUsernames(){
		ArrayList<String> list = new ArrayList<String>();
		for (int i=0; i<players.size(); i++){
			if (players.get(i) != null && players.get(i).getID() != id){
				list.add(players.get(i).getUsername());
			}
		}
		return list;
	}
	
	public int playerSize(){
		return players.size();
	}
	
	public ArrayList<String[]> getChat(){
		return chat;
	}
	
	public void addChat(int id, String message){
		chat.add(new String[]{players.get(id).getUsername(), message});
	}
	
	/*public void updateUnits(int delta){
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
	}*/
}
