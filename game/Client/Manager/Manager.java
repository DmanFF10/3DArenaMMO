package Client.Manager;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import Client.Listener.Listener;
import Client.Manager.Callbacks.listenerCBs;
import Client.Manager.Callbacks.visualizerCBs;
import Client.Visualizer.Visualizer;
import GameLibrary.*;
/*
 * manages the clients game
 * sends and receives data to and from the listener
 */
import GameLibrary.Character;
import GameLibrary.util.Consts;
import GameLibrary.util.Loader;
import GameLibrary.util.Logger;
import GameLibrary.util.Thing;

public class Manager {
	

	public static enum State {
		MainMenu, Game
	}
	
	private State state = State.MainMenu;
	
	private boolean live = true;
	private int port = 1234;
	private String address = "127.0.0.1";
	private GameClient game;
	private Listener sender;
	private Visualizer view;
	
	public Manager(){
		// stars logging
		Logger.startLogger("Client");
		startVisualizer();
	}
	
	public void startVisualizer(){
		// initialize the game view
		view = new Visualizer(vizCBs());
		Logger.log(Logger.INFO, "Starting graphical front end");
		view.start();
	}
	
	private void startListener(){
		sender = new Listener(address, port, initCBs());
		Logger.log(Logger.INFO, "Starting listener");
		sender.start();
	}
	
	private void startGame(String username){
		// Initialize game data container
		Logger.log(Logger.INFO, "Setting up game");
		game = new GameClient(username);
	}
	
	private listenerCBs initCBs(){
		// returns the callback functions for the listener object 
		return new listenerCBs() {
			
			// returns the state of the program
			public boolean isLive() {
				return live;
			} 
			
			public boolean isConnected() {
				return game.getID() != -1;
			}
			
			// does initial connection operations
			public Thing initConnect(){
				Command data = new Command(game.getID(), game.getName());
				return data;
			}
			
			@SuppressWarnings("unchecked")
			public void identifyPackage(Thing data){
				int id = data.getID();
				String username = data.getUsername();
				Command cmd;
				switch(data.getType()){
			    	
			    	case Consts.TYPE_MOVE:
			    		cmd = (Command)data;
			    		Character player = game.getCharacter(id);
			    		player.movement = cmd.getMovement();
			    		player.object.position = cmd.getPosition();
			    		player.object.rotation = cmd.getRotation();
			    		break;
			    		
			    	case Consts.TYPE_NEW_PlAYER:
			    		Character character = (Character)((Command)data).getObject();
			    		if(id < game.playerSize()){
			    			game.setCharacter(id, character);
			    		} else {
			    			game.addCharacter(character);
			    		}
			    		break;
			    	
			    	case Consts.TYPE_MAP:
			    		game.map = Loader.readMap((ArrayList<String>)((Command)data).getObject());
			    		cmd = (Command)data;
			    		game.setID(id);
			    		game.setName(username);
			    		state = State.Game;
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
			
			public void connect(String username) {
				startGame(username);
			}
			
			public void requestMove(Vector3f direction, Vector3f rotation){
				sender.send(new Command(game.getID(), game.getName(), direction, rotation));
			}
			
			public State state(){
				return state;
			}
			
			// returns the game object
			public GameClient game(){
				return game;
			}
		};
	}
}
