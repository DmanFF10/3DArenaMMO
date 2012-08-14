package Server;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import GameLibrary.*;
import GameLibrary.Character;

public class Manager {
	/*
	 * controls the games input and output
	 */
	
	// callback declaration
	public interface listenerCBs{
		boolean isLive();
		void identifyPackage(Thing data);
	}
	
	// management variables
	private boolean live = true;
	private int port = 1234;
	private GameServer game;
	private Listener sender;
	
	// constructors
	public Manager() {
		Logger.startLogger("Server");
		
		game = new GameServer("test");
		sender = new Listener(port, initCBs());
		sender.Start();
	}
		
	private listenerCBs initCBs(){
		 return new listenerCBs() {
			
			public boolean isLive() {
				return live;
			}
			
			public void identifyPackage(Thing data){
				int id = data.getID();
				String username = data.getUsername();
				Command cmd;
				switch(data.getType()){
			    	
					case Consts.TYPE_LOGIN:
			    		// add a new character to the game
			    		game.addCharacter();
			    		// send the mapstring to the client
			    		for(String string : game.map.mapstring){
			    			sender.send(new Command(id, username, string), id);
			    		}
			    		// send all the character objects to the client
			    		ArrayList<Character> characters = game.getCharacters();
			    		for(Character player : characters){
			    			sender.send(new Command(id, username, player), id);
			    		}
			    		// create and send the login object back to client
			    		sender.send(new Login(id, username), id);
			    		// broadcast to everyone that a new player has joined
			    		sender.broadcast(new Command(id, username, game.getCharacter(id)));
			    		break;
			    		
			    	case Consts.TYPE_MOVE:
			    		cmd = (Command)data;
			    		Character player = game.getCharacter(cmd.getID());
			    		Vector3f movement = cmd.getMovement();
			    		// set the players rotation
			    		player.object.rotation = cmd.getRotation();
			    		// update the players location
			    		game.updateUnit(cmd.getID(), cmd.getTimeStamp(), movement);
			    		// set what direction the player is moving
			    		player.movement = movement;
			    		// update the characters changes
			    		game.setCharacter(cmd.getID(), player);
			    		// create new move command
			    		Command newCmd = new Command(cmd.getID(), cmd.getUsername(), player.object.position, player.movement, cmd.getRotation());
			    		// give command to all users
			    		sender.broadcast(newCmd);
			    		break;
				}
			}
		};
	}
}
