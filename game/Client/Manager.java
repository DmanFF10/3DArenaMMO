package Client;

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
		game = new GameClient("GaugeII");
		// initialize the game view
		view = new Visualizer(vizCBs());
		view.start();
		sender = new Listener(address, port, initCBs());
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
				Login data = new Login(game.getID(), game.getName());
				return data;
			}
			
			public void identifyPackage(Thing data){
			    switch(data.getType()){
			    	
			    	case Consts.TYPE_LOGIN:
			    		Login login = (Login)data;
			    		game.setID(login.getID());
			    		game.setName(login.getUsername());
			    		view.setState(State.Connected);
			    		break;
			    	
			    	case Consts.TYPE_MOVE:
			    		Command cmd = (Command)data;
			    		Character player = game.getCharacter(cmd.getID());
			    		player.movement = cmd.getMovement();
			    		player.object.position = cmd.getPosition();
			    		player.object.rotation = cmd.getRotation();
			    		break;
			    		
			    	case Consts.TYPE_NEW_PlAYER:
			    		if(data.getID() < game.playerSize()){
			    			game.setCharacter(data.getID(), (Character)((Command)data).getObject());
			    		} else{
			    			game.addCharacter((Character)((Command)data).getObject());
			    		}
			    		break;
			    	
			    	case Consts.TYPE_MAP:
			    		String newString = (String)((Command)data).getObject();
			    		game.map.mapstring.add(newString);
			    		if(newString.equals("END")){
			    			game.map = Loader.readMap(game.map.mapstring);
			    		}
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
				Command cmd = new Command(game.getID(), game.getName(), direction, rotation);
				sender.send(cmd);
			}
			
			// returns the game object
			public GameClient game(){
				return game;
			}
		};
	}
}
