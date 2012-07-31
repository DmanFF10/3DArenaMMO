package Client;

import Client.Callbacks.listenerCBs;
import Client.Callbacks.visualizerCBs;
import Client.Visualizer.State;
import GameLibrary.*;
/*
 * manages the clients game
 * sends and receives data to and from the listener
 */

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
			    		game.setCharacters(login.getCharacters());
			    		game.map = login.getMap();
			    		view.setState(State.Connected);
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
			
			// returns the game object
			public GameClient game(){
				return game;
			}
		};
	}
}
