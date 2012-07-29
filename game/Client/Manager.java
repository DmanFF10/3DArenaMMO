package Client;

import java.util.ArrayList;
import org.json.JSONObject;

import Client.Callbacks.listenerCBs;
import Client.Callbacks.visualizerCBs;
import GameLibrary.*;
import GameLibrary.Character;
/*
 * manages the clients game
 * sends and receives data to and from the listener
 */

public class Manager {
	
	private boolean live = true;
	private int port;
	private String address;
	private GameClient game;
	private Listener sender;
	private Visualizer view;
	
	public Manager(){
		Logger.log(Logger.INFO, "Client Started");
		port = 1234;
		address = "127.0.0.1";
		game = new GameClient();
		view = new Visualizer(vizCBs());
		view.start();
		//sender = new Listener(address, port, initCBs());
		//sender.start();
	}
	
	public Manager(String address, int port){
		this.port = port;
		this.address = address;
		view = new Visualizer(vizCBs());
		view.start();
		//game = new GameClient();
		//sender = new Listener(address, port, initCBs());
		//sender.start();
	}
	
	private listenerCBs initCBs(){
		// returns the callback functions for the listener object 
		return new listenerCBs() {
			// returns the state of the program
			public boolean isLive() {
				return live;
			} 
			
			// does initial connection operations
			public void initConnect(){}
			
			@SuppressWarnings("unchecked")
			public void identifyPackage(JSONObject data){
				// get the type and do appropriate operations
				int type = data.getInt("type");
			    switch(type){
					
			    	case Consts.TYPE_LOGIN:
			    		game.setCharacters((ArrayList<Character>)data.get("characters"));
			    		game.map = (Map)data.get("map");
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
			public Map map(){
				return game.map;
			}
		};
	}
}
