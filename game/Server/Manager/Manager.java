package Server.Manager;

import java.net.DatagramPacket;
import GameLibrary.*;
import GameLibrary.util.Consts;
import GameLibrary.util.Logger;
import GameLibrary.util.Packer;
import Server.Listener.Client;
import Server.Listener.Listener;

public class Manager {
	/*
	 * controls the games input and output
	 */
	
	// callback declaration
	public interface listenerCBs{
		boolean isLive();
		void process(DatagramPacket p);
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
	
	public void processCommand(Command data){
		switch(data.getCommandType()){
	    	
			case Consts.TYPE_LOGIN_PASS:
				// add a new character to the game
	    		game.addCharacter();
	    		// broadcast to everyone that a new player has joined
	    		sender.broadcast(Packer.pack(data));
	    		break;
		}
	}
		
	private listenerCBs initCBs(){
		 return new listenerCBs() {
			
			public boolean isLive() {
				return live;
			}
			
			public void process(DatagramPacket data){
				String[] values = new String(data.getData()).split(Consts.PACK_SPLITER);
				try{
					int id = Integer.parseInt(values[0]);
					if (id == Consts.DISCONNECTED){
						String username = values[1];
						//TODO: make the password usefull
						String password = values[2];
						// if not a connected user add to clients
						sender.clients.add(new Client(data.getAddress(), data.getPort()));
						// creates a new object with the clients new id
						Command cmd = new Command(sender.clients.size()-1, username);
						Logger.log(Logger.INFO, "User " + username + " has logged in with the id of: " + cmd.getID());
						// sends package to be utilized by the game
						processCommand(cmd);
					}else{
						
					}
				} catch(Exception e){}
			}
		};
	}
}
