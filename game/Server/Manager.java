package Server;

import GameLibrary.*;

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
			    switch(data.getType()){
			    	
			    	case Consts.TYPE_LOGIN:
			    		game.addCharacter();
			    		Login login = new Login(data.getID(), data.getUsername(), game.getCharacters(), game.map);
			    		sender.send(login, data.getID());
			    		break;
				}
			}
		};
	}
}
