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
	private boolean live = true;
	private int port;
	private GameServer game;
	private Listener sender;
	
	// constructors
	public Manager() {
		port = 1234;
		game = new GameServer();
		sender = new Listener(port, initCBs());
		sender.Start();
	}
	
	public Manager(int port){
		this.port = port;
		game = new GameServer();
		sender = new Listener(port, initCBs());
		sender.Start();
	}
		
	private listenerCBs initCBs(){
		 return new listenerCBs() {
			
			public boolean isLive() {
				return live;
			}
			
			public void identifyPackage(JSONObject data){
				// get the type and do appropriate operations
				int type = data.getInt("type");
			    switch(type){
					
			    	case Consts.TYPE_LOGIN:
			    		// create login object to send back to the client with startup information
			    		data = CreateJson.Login(data.getInt("id"), data.getString("username"), 
			    									game.getCharacters(), game.map);
			    		// send data to client
						sender.send(data.toString(), data.getInt("id"));
			    		break;
				}
			}
		};
	}
}
