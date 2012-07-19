package Server;
import org.json.JSONObject;

import GameLibrary.*;

public class Manager {
	/*
	 * controls the games input and output
	 */
	
	// callback declaration
	public interface listenerCBs{
		boolean isLive();
		void identifyPackage(JSONObject data);
	}
	
	// management variables
	private boolean live;
	private int port;
	private GameServer game;
	private Listener sender;
	
	// constructors
	public Manager() {
		live = true;
		port = 1234;
		sender = new Listener(port, initCBs());
	}
	
	public Manager(int port){
		live = true;
		this.port = port;
		sender = new Listener(port, initCBs());
	}
	
	// TODO: create operations on the game
		
	private listenerCBs initCBs(){
		 return new listenerCBs() {
			
			public boolean isLive() {
				return live;
			}
			
			public void identifyPackage(JSONObject data){
				int type = data.getInt("type");
			    switch(type){
					
			    	case Consts.TYPE_LOGIN:
				    	data.put("characters", game.getCharacters());
						data.put("map", game.map);
						sender.send(data.toString(), data.getInt("id"));
			    		break;
				}
			}};
	}
}
