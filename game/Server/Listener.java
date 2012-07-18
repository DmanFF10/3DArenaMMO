package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.ParseException;
import java.util.ArrayList;
import org.json.JSONObject;

import GameLibrary.Consts;

public class Listener {
	
	private DatagramSocket server;
	private Manager.listenerCBs cbs;
	private ArrayList<Client> clients = new ArrayList<Client>();
	
	public Listener(int port, Manager.listenerCBs cbs){
		System.out.println("Starting server...");
		this.cbs = cbs;
		
		try{
			server = new DatagramSocket(port);
			System.out.println("Listening for clients...");
			listen();
		} catch(Exception e) {
			System.out.println("Error: port in use");
		}
	}
	
	private void listen() {
		// creates datagram packet
		byte[] data = new byte[2024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		
		// while server is running
		while(cbs.isLive()){
			try{
				// receives packages from client
				server.receive(packet);
			    process(packet);
			
			} catch(IOException e){
				System.out.print("Error: package issue");
			}
		}
	}
	
	private boolean process(DatagramPacket packet){
		// get a json object
		JSONObject data = extractData(packet);
		// check to see if userID is a connected user
		int id = data.getInt("id");
		if (id == Consts.DISCONNECTED){
			// if not a connected user add to clients
			clients.add(new Client(packet.getAddress(), packet.getPort()));
			// change the userID value to its new value
			data.put("userID", clients.size()-1);
		}
		cbs.identifyPackage(data);
		return true;
	} 
	
	public void send(String value, int id){
		// packages message for transport
		byte[] data = value.getBytes();
		// gets client address and port and creates a packet with the data and location
		Client person = clients.get(id);
		DatagramPacket packet = new DatagramPacket(data, data.length, person.Address(), person.Port());
		
		try {
			//sends packet
			server.send(packet);
		} catch (IOException e) {
			System.out.print("failed to send package to " + id +" containing: \n\n" + value);
		}
	}
	
	public void broadcast(String value){
		// packages message into bytes for transport
		byte[] data = value.getBytes();
		// loops through all the clients
		for (int i=0; i<clients.size(); i++){
			// gets client address and port and creates a packet with the data and location
			Client person = clients.get(i);
			DatagramPacket packet = new DatagramPacket(data, data.length, person.Address(), person.Port());
			
			try{
				// sends packet
				server.send(packet);
			} catch(IOException e){
				System.out.print("failed to send to user " + i);
			}
			
		}
	}
	
	
	private JSONObject extractData(DatagramPacket packet){
		// returns the string from the package
		try{
		return new JSONObject(packet.getData().toString());
		} catch(Exception e) {
			return null;
		}
	}
}
