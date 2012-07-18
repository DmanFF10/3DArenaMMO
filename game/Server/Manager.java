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
	private Listener listener;
	
	// constructors
	public Manager() {
		live = true;
		port = 1234;
		listener = new Listener(port, initCBs());
	}
	
	public Manager(int port){
		live = true;
		this.port = port;
		listener = new Listener(port, initCBs());
	}
	
	// TODO: create operations on the game
		
	private listenerCBs initCBs(){
		 return new listenerCBs() {
			
			public boolean isLive() {
				return live;
			}
			
			public void identifyPackage(JSONObject data){
				
			}};
	}
}
