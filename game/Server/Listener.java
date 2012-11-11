package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

import GameLibrary.Command;
import GameLibrary.Consts;
import GameLibrary.Logger;
import GameLibrary.Serializer;
import GameLibrary.Thing;

/*
 * listens for client activities
 */

public class Listener {

	private DatagramSocket server;
	private Manager.listenerCBs cbs;
	private ArrayList<Client> clients = new ArrayList<Client>();
	
	public Listener(int port, Manager.listenerCBs cbs){
		this.cbs = cbs;
		Logger.log(Logger.INFO, "Starting Server");
		
		try{
			// create a server that is listening at the specified port
			server = new DatagramSocket(port);
		} catch(Exception e) {
			Logger.log(Logger.ERROR, "Failed to start server");
		}
	}
	
	public void Start() {
		Logger.log(Logger.INFO, "Listening for clients");
		// creates datagram packet
		byte[] data = new byte[2024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		
		// while server is running
		while(cbs.isLive()){
			try{
				// receives packages from client
				server.receive(packet);
			    // sends the packet to be processed
				process(packet);
			} catch(IOException e){
				Logger.log(Logger.ERROR, "failed to receive packet");
			}
		}
	}
	
	private void process(DatagramPacket packet){
		try{
			Thing object = (Thing)Serializer.unpack(packet.getData());
			int id = object.getID();
			if (id == Consts.DISCONNECTED){
				// if not a connected user add to clients
				clients.add(new Client(packet.getAddress(), packet.getPort()));
				// creates a new object with the clients new id
				Command login = new Command(clients.size()-1, object.getUsername());
				Logger.log(Logger.INFO, "User " + login.getUsername() + " has logged in with the id of: " + login.getID());
				// sends package to be utilized by the game
				cbs.identifyPackage(login);
	
			}else{
				// sends package to be utilized by the game
				cbs.identifyPackage(object);
			}
		}catch(Exception e){
			Logger.log(Logger.ERROR, "unpacking failed");
		}
	} 
	
	public void send(Thing value, int id){
		try {
			// packages message for transport
			byte[] data = Serializer.pack(value);
			// gets client address and port and creates a packet with the data and location
			Client person = clients.get(id);
			DatagramPacket packet = new DatagramPacket(data, data.length, person.Address(), person.Port());
			
			//sends packet
			server.send(packet);
		} catch (IOException e) {
			Logger.log(Logger.ERROR, "failed to send package to " + id +" containing: \n\n" + value);
		}
	}
	
	public void broadcast(Thing value){
		try{
			// packages message into bytes for transport
			byte[] data = Serializer.pack(value);
			// loops through all the clients
			for (int i=0; i<clients.size(); i++){
				// gets client address and port and creates a packet with the data and location
				Client person = clients.get(i);
				DatagramPacket packet = new DatagramPacket(data, data.length, person.Address(), person.Port());
				// sends packet
				server.send(packet);
			}
		} catch(IOException e){
			Logger.log(Logger.ERROR, "failed to send to all the users");
		}
	}
}
