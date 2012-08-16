package Client;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import Client.Callbacks.listenerCBs;
import Client.Callbacks.visualizerCBs;
import Client.Visualizer.State;
import GameLibrary.*;
/*
 * manages the clients game
 * sends and receives data to and from the listener
 */
import GameLibrary.Character;

public class Manager {
	
	private boolean live = true;
	private int port = 1234;;
	private String address = "127.0.0.1";
	private GameClient game;
	private Listener sender;
	private Visualizer view;
	
	public Manager(){
		// stars logging
		Logger.startLogger("Client");
		
		// Initialize game data container
		Logger.log(Logger.INFO, "Setting up game");
		game = new GameClient("GaugeII");
		// initialize the game view
		view = new Visualizer(vizCBs());
		Logger.log(Logger.INFO, "Starting graphical front end");
		view.start();
		sender = new Listener(address, port, initCBs());
		Logger.log(Logger.INFO, "Starting listener");
		sender.start();
	}
	
	private listenerCBs initCBs(){
		// returns the callback functions for the listener object 
		return new listenerCBs() {
			
			// returns the state of the program
			public boolean isLive() {
				return live;
			} 
			
			// does initial connection operations
			public Thing initConnect(){
				Command data = new Command(game.getID(), game.getName());
				return data;
			}
			
			@SuppressWarnings("unchecked")
			public void identifyPackage(Thing data){
				Command cmd;
				switch(data.getType()){
			    	case Consts.TYPE_LOGIN:
			    		cmd = (Command)data;
			    		game.setID(cmd.getID());
			    		game.setName(cmd.getUsername());
			    		view.setState(State.Connected);
			    		break;
			    	
			    	case Consts.TYPE_MOVE:
			    		cmd = (Command)data;
			    		Character player = game.getCharacter(cmd.getID());
			    		player.movement = cmd.getMovement();
			    		player.object.position = cmd.getPosition();
			    		player.object.rotation = cmd.getRotation();
			    		break;
			    		
			    	case Consts.TYPE_NEW_PlAYER:
			    		Character character = (Character)((Command)data).getObject();
			    		if(data.getID() < game.playerSize()){
			    			game.setCharacter(data.getID(), character);
			    		} else{
			    			game.addCharacter(character);
			    		}
			    		break;
			    	
			    	case Consts.TYPE_MAP:
			    		game.map = Loader.readMap((ArrayList<String>)((Command)data).getObject());
			    		break;
			    }
			}
		};
	}
	
	
	
	private visualizerCBs vizCBs(){
		// returns the callback functions for the visualizer object
		return new visualizerCBs(){
			// sets the state of the program to false
			public void endLive(){
				live = false;
			}
			
			public void requestMove(Vector3f direction, Vector3f rotation){
				sender.send(new Command(game.getID(), game.getName(), direction, rotation));
			}
			
			public void disconnect(){
				sender.send(new Command(game.getID(), game.getName()));
			}
			
			// returns the game object
			public GameClient game(){
				return game;
			}
		};
	}
}
