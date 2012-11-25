package Server.Manager;

import java.net.DatagramPacket;
import java.util.ArrayList;

import GameLibrary.*;
import GameLibrary.util.Logger;
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
	private ArrayList<ArrayList<String>> messages = new ArrayList<ArrayList<String>>();
	
	// constructors
	public Manager() {
		Logger.startLogger("Server");
		game = new GameServer("test");
		sender = new Listener(port, initCBs());
		sender.Start();
	}
	
	private void addMessage(int id){
		while(messages.size() <= id){
			messages.add(null);
		}
		messages.set(id ,new ArrayList<String>());
	}
	
	public void removeMessage(int id){
		messages.set(id, null);
	}
	
	public void processCommand(Command data){
		int id;
		switch(data.getCommandType()){
			case Consts.TYPE_LOGIN_PASS:
				// add a new character to the game
	    		game.addPlayer(data.getID(), data.getUsername());
	    		// broadcast to everyone that a new player has joined
	    		sender.broadcast(data.pack());
	    		// sends all the players currently active to the newly connected client
	    		for (int i = 0; i<game.playerSize(); i++){
	    			sender.send(new Command(i, game.getPlayer(i).getUsername()).pack(), data.getID());
	    		}
	    		break;	
			
			case Consts.TYPE_LOGOUT:
				id = data.getID();
				sender.broadcast(data.pack());
				Logger.log(Logger.INFO, "User " + game.getPlayer(id).getUsername() + " has logged off");
				removeMessage(id);
				game.removePlayer(id);
				sender.removeClient(id);
				break;
				
			case Consts.TYPE_MESSAGE:
				if (data.getMessageType() == Consts.MESSAGE_ALL){
					sender.broadcast(data.pack());
				}
				break;
		}
	}
		
	private listenerCBs initCBs(){
		 return new listenerCBs() {
			
			public boolean isLive() {
				return live;
			}
			
			public void process(DatagramPacket data){
				String string = new String(data.getData()).trim();
				String[] values = string.split(Consts.PACK_SPLITER);
				try{
					int id = Integer.parseInt(values[0]);
					if (id == Consts.DISCONNECTED){
						String username = values[1];
						//TODO: make the password usefull
						String password = values[2];
						int newID = game.getNewPlayerID();
						// if not a connected user add to clients
						sender.addClient(newID, new Client(data.getAddress(), data.getPort()));
						// creates a new object with the clients new id
						Command cmd = new Command(newID, username);
						Logger.log(Logger.INFO, "User " + username + " has logged in with the id of: " + cmd.getID());
						addMessage(newID);
						// sends package to be utilized by the game
						processCommand(cmd);
					
					} else if (Command.isEnd(string)){
						messages.get(id).add(string);
						processCommand(Command.unpack(messages.get(id)));
						messages.set(id, new ArrayList<String>());
					} else {
						messages.get(id).add(string);
					}
				} catch(Exception e){}
			}
		};
	}
}
