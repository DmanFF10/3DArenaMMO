package Client;

import java.util.ArrayList;
import org.json.JSONObject;
import GameLibrary.*;
import GameLibrary.Character;
/*
 * manages the clients game
 * sends and receives data to and from the listener
 */

public class Manager {
	
	public interface listenerCBs{
		boolean isLive();
		void initConnect();
		void identifyPackage(JSONObject data);
	}
	
	private boolean live = true;
	private int port;
	private String address;
	private GameClient game;
	private Listener sender;
	private Visualizer view;
	
	public Manager(){
		port = 1234;
		address = "127.0.0.1";
		game = new GameClient();
		sender = new Listener(address, port, initCBs());
		view = new Visualizer();
		sender.start();
	}
	
	public Manager(String address, int port){
		this.port = port;
		this.address = address;
		game = new GameClient();
		sender = new Listener(address, port, initCBs());
		view = new Visualizer();
		sender.start();
	}
	
	private listenerCBs initCBs(){
		 return new listenerCBs() {
			
			public boolean isLive() {
				return live;
			}
			
			public void initConnect(){
				
			}
			
			public void identifyPackage(JSONObject data){
				// get the type and do appropriate operations
				int type = data.getInt("type");
			    switch(type){
					
			    	case Consts.TYPE_LOGIN:
			    		game.setCharacters((ArrayList<Character>)data.get("characters"));
			    		game.map = (Map)data.get("map");
			    		break;
				}
			}};
	}
}
