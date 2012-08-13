package GameLibrary;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

/*
 * were all the magic happens for the client
 * stores all the game values such as maps characters and more
 */

public class GameServer {
	
	private ArrayList<Character> characters = new ArrayList<Character>();
	public Map map;
	
	public GameServer(String mapname){
		map = Loader.readMap(mapname);
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
	
	public void addCharacter(){
		characters.add(new Character(0f, 0f, 0f));
	}
	
	public void updateUnit(int id, long time, Vector3f movement){
		Character player = getCharacter(id);
		int delta;
		if (player.movement.x == Consts.MOVE_BACKWORD_RIGHT && movement.x == Consts.MOVE_STOP){
			delta = (int) (time - player.timex);
			float angle = player.object.rotation.y;
			float speed = (delta*(player.speed * Consts.UNITSIZE));
			player.object.position.z += (float) Math.sin(Math.toRadians(angle)) * speed;
			player.object.position.x += (float) Math.cos(Math.toRadians(angle)) * speed;
		
		} if(player.movement.x == Consts.MOVE_FORWORD_LEFT && movement.x == Consts.MOVE_STOP){
			delta = (int) (time - player.timex);
			float angle = player.object.rotation.y;
			float speed = (delta*(player.speed * Consts.UNITSIZE));
			player.object.position.z -= (float) Math.sin(Math.toRadians(angle)) * speed;
			player.object.position.x -= (float) Math.cos(Math.toRadians(angle)) * speed;
		
		} if(player.movement.z == Consts.MOVE_BACKWORD_RIGHT && movement.z == Consts.MOVE_STOP){
			delta = (int) (time - player.timez);
			float angle = player.object.rotation.y;
			float speed = (delta*(player.speed * Consts.UNITSIZE));
			player.object.position.x += (float) Math.sin(Math.toRadians(angle)) * speed;
			player.object.position.z += (float) Math.cos(Math.toRadians(angle)) * speed;
		
		} if(player.movement.z == Consts.MOVE_FORWORD_LEFT && movement.z == Consts.MOVE_STOP){
			delta = (int) (time - player.timez);
			float angle = player.object.rotation.y;
			float speed = (delta*(player.speed * Consts.UNITSIZE));
			player.object.position.x -= (float) Math.sin(Math.toRadians(angle)) * speed;
			player.object.position.z -= (float) Math.cos(Math.toRadians(angle)) * speed;
		
		} if (movement.x == Consts.MOVE_BACKWORD_RIGHT && player.movement.x == Consts.MOVE_STOP || 
				movement.x == Consts.MOVE_FORWORD_LEFT && player.movement.x == Consts.MOVE_STOP){
			player.timex = time;
		} if (movement.z == Consts.MOVE_BACKWORD_RIGHT && player.movement.z == Consts.MOVE_STOP || 
				movement.z == Consts.MOVE_FORWORD_LEFT && player.movement.z == Consts.MOVE_STOP){
			player.timez = time;
		}
	}
	
}
