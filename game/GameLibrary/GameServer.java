package GameLibrary;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import GameLibrary.util.Consts;
import GameLibrary.util.Loader;

/*
 * were all the magic happens for the client
 * stores all the game values such as maps characters and more
 */

public class GameServer {
	
	private ArrayList<Player> players = new ArrayList<Player>();
	public Map map;
	
	public GameServer(String mapname){
		map = Loader.readMap(mapname);
	}
	
	public ArrayList<Player> getPlayers(){
		return players;
	}
	
	public Player getPlayer(int id){
		return players.get(id);
	}
	
	public void addPlayer(int id, String username){
		players.add(new Player(id, username));
	}
	
	/*public void updateUnit(int id, long time, Vector3f movement, Vector3f rotation){
		Character player = getCharacter(id);
		int delta;
		boolean rotated = false;
		if (player.object.rotation.x != rotation.x || player.object.rotation.y != rotation.y){
			rotated = true;
		}
		if (player.movement.x == Consts.MOVE_BACKWORD_RIGHT && movement.x == Consts.MOVE_STOP || 
				rotated && player.movement.x == Consts.MOVE_BACKWORD_RIGHT){
			delta = (int) (time - player.timex);
			float angleX = player.object.rotation.x;
			float angleY = player.object.rotation.y;
			float speed = (delta*(player.speed * Consts.UNITSIZE));
			player.object.position.x += ((float) Math.cos(Math.toRadians(angleY)) * speed); 
			player.object.position.y -= ((float) Math.sin(Math.toRadians(angleX)) * speed); 
			player.object.position.z -= ((float) Math.sin(Math.toRadians(angleY)) * speed);
		
		} if(player.movement.x == Consts.MOVE_FORWORD_LEFT && movement.x == Consts.MOVE_STOP ||
				rotated && player.movement.x == Consts.MOVE_FORWORD_LEFT){
			delta = (int) (time - player.timex);
			float angleX = player.object.rotation.x;
			float angleY = player.object.rotation.y;
			float speed = (delta*(player.speed * Consts.UNITSIZE));
			player.object.position.x -= ((float) Math.cos(Math.toRadians(angleY)) * speed); 
			player.object.position.y += ((float) Math.sin(Math.toRadians(angleX)) * speed); 
			player.object.position.z += ((float) Math.sin(Math.toRadians(angleY)) * speed);
		
		} if(player.movement.z == Consts.MOVE_BACKWORD_RIGHT && movement.z == Consts.MOVE_STOP ||
				rotated && player.movement.z == Consts.MOVE_BACKWORD_RIGHT){
			delta = (int) (time - player.timez);
			float angleX = player.object.rotation.x;
			float angleY = player.object.rotation.y;
			float speed = (delta*(player.speed * Consts.UNITSIZE));
			player.object.position.x += ((float) Math.sin(Math.toRadians(angleY)) * speed); 
			player.object.position.y -= ((float) Math.sin(Math.toRadians(angleX)) * speed); 
			player.object.position.z += ((float) Math.cos(Math.toRadians(angleY)) * speed);
		
		} if(player.movement.z == Consts.MOVE_FORWORD_LEFT && movement.z == Consts.MOVE_STOP ||
				rotated && player.movement.z == Consts.MOVE_FORWORD_LEFT){
			delta = (int) (time - player.timez);
			float angleX = player.object.rotation.x;
			float angleY = player.object.rotation.y;
			float speed = (delta*(player.speed * Consts.UNITSIZE));
			player.object.position.x -= ((float) Math.sin(Math.toRadians(angleY)) * speed); 
			player.object.position.y += ((float) Math.sin(Math.toRadians(angleX)) * speed); 
			player.object.position.z -= ((float) Math.cos(Math.toRadians(angleY)) * speed);
		
		} if (movement.x == Consts.MOVE_BACKWORD_RIGHT && player.movement.x == Consts.MOVE_STOP || 
				movement.x == Consts.MOVE_FORWORD_LEFT && player.movement.x == Consts.MOVE_STOP ||
				rotated){
			player.timex = time;
		} if (movement.z == Consts.MOVE_BACKWORD_RIGHT && player.movement.z == Consts.MOVE_STOP || 
				movement.z == Consts.MOVE_FORWORD_LEFT && player.movement.z == Consts.MOVE_STOP ||
				rotated){
			player.timez = time;
		}
		setCharacter(id, player);
	}*/
}
