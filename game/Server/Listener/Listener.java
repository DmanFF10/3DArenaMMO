package Server.Listener;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

import GameLibrary.util.Logger;
import Server.Manager.Manager;
import Server.Manager.Manager.listenerCBs;

/*
 * listens for client activities
 */

public class Listener {

	private DatagramSocket server;
	private listenerCBs cbs;
	public ArrayList<Client> clients = new ArrayList<Client>();
	
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
		byte[] data = new byte[512];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		
		// while server is running
		while(cbs.isLive()){
			try{
				// receives packages from client
				server.receive(packet);
			    // sends the packet to be processed
				cbs.process(packet);
		    	// resets the byte buffer
				data = new byte[512];
		    	packet.setData(data);
		    	
			} catch(IOException e){
				Logger.log(Logger.ERROR, "failed to receive packet");
			}
		}
	}
	
	public void send(ArrayList<String> info, int id){
		try {
			// gets client address and port and creates a packet with the data and location
			Client person = clients.get(id);
			for(String value : info){
				// packages message for transport
				byte[] data = value.getBytes();
				DatagramPacket packet = new DatagramPacket(data, data.length, person.Address(), person.Port());
				//sends packet
				server.send(packet);
			}
		} catch (IOException e) {
			Logger.log(Logger.WORNING, "Package Interupt failed to send everything to playerID:" + id);
		}
	}
	
	public void broadcast(ArrayList<String> info){
		DatagramPacket packet;
		try{
			// loops through all the clients
			for (int i=0; i<clients.size(); i++){
				// gets client address and port and creates a packet with the data and location
				Client person = clients.get(i);
				// for each string in the array
				for(String value : info){
					Logger.log(Logger.DEBUG, "sending: " + value);
					// packages message into bytes for transport
					byte[] data = value.getBytes();
					packet = new DatagramPacket(data, data.length, person.Address(), person.Port());
					// sends packet
					server.send(packet);
				}
			}
		} catch(IOException e){
			Logger.log(Logger.WORNING, "Package Interupt failed to send to all data");
		}
	}
}
