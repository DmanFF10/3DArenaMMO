package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class Listener {
	
	private DatagramSocket server;
	private Manager.listenerCBs cbs;
	private ArrayList clients;
	
	public Listener(int port, Manager.listenerCBs cbs){
		System.out.println("Starting server...");
		this.cbs = cbs;
		
		try{
			server = new DatagramSocket(port);
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
				System.out.print(extractData(packet));
				//TODO: send data to be processed
			} catch(IOException e){
				System.out.print("Error: package issue");
			}
		}
	}
	
	public void send(String value, int id){
		// packages message for transport
		byte[] data = value.getBytes();
		//TODO: send package to specified address and port
		DatagramPacket packet = new DatagramPacket(data, data.length);
		
		//sends packet
		try {
			server.send(packet);
		} catch (IOException e) {
			System.out.print("failed to send package containing: \n\n" + value);
		}
	}
	
	private String extractData(DatagramPacket packet){
		// returns the string from the package
		return packet.getData().toString();
	}
	
}
