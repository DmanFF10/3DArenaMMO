package Client.Listener;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import Client.Manager.Callbacks;
import GameLibrary.util.Logger;


/*
 * client listener
 * gets data sent for the server and sends it to be processed
 */

public class Listener extends Thread {
	
	/*
	 * sends and receives data from server
	 */
	
	private int port;
	private InetAddress address;
	private Callbacks.listenerCBs cbs;
	private DatagramSocket client;
	
	public Listener(String address, int port, Callbacks.listenerCBs cbs){
		this.port = port;
		this.cbs = cbs;
		try {
			client = new DatagramSocket();
			client.setSoTimeout(2000);
			this.address = InetAddress.getByName(address);
		} catch (Exception e) {
			Logger.log(Logger.ERROR, "failed to initalize listener");
		}
	}

	public void run(){
		// sets up package
		byte[] data = new byte[512];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		
		while(cbs.isLive()){
		    try{
		    	// receives package and process
		    	client.receive(packet);
		    	cbs.process(new String(packet.getData()));
		    	data = new byte[512];
		    	packet.setData(data);
		    	
		    }catch(Exception e){}
		    
		}
		Logger.log(Logger.INFO, "Ending listener operations"); 
		
	}
	
	public void send(ArrayList<String> info){
		try{
			for(String value : info){
				byte[] data = value.getBytes();
				DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
				client.send(packet);	
			}	
		} catch(Exception e){
			Logger.log(Logger.WORNING, "Packet Interuption");
		}
	}
}
