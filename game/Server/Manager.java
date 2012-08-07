package Server;

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
				Command cmd;
				switch(data.getType()){
			    	case Consts.TYPE_LOGIN:
			    		game.addCharacter();
			    		Login login = new Login(data.getID(), data.getUsername(), game.getCharacters(), game.map);
			    		cmd = new Command(data.getID(), data.getUsername(), game.getCharacter(data.getID()));
			    		sender.send(login, data.getID());
			    		sender.broadcast(cmd);
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
