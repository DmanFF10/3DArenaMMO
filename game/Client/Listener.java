package Client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import GameLibrary.Logger;
import GameLibrary.Serializer;
import GameLibrary.Thing;


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
			
		} catch (Exception e) {}
	}

	public void run(){
		// send connection request
		send(cbs.initConnect());
		
		// sets up package
		byte[] data = new byte[2048];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		
		while(cbs.isLive()){
		    try{
		    	// receives package and process
		    	client.receive(packet);
		    	cbs.identifyPackage((Thing)Serializer.unpack(packet.getData()));
		    
		    }catch(Exception e){}
		}
		Logger.log(Logger.INFO, "Ending listener operations"); 
		
	}
	
	public void send(Thing object){
		try{
			byte[] data = Serializer.pack(object);
			DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
			client.send(packet);		
		} catch(Exception e){
			Logger.log(Logger.ERROR, "Failed to send packet");
		}
	}
}
