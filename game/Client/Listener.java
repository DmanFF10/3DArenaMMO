package Client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.json.JSONObject;
import GameLibrary.Consts;
import GameLibrary.CreateJson;


/*
 * client listener
 * gets data sent for the server and sends it to be processed
 */

public class Listener extends Thread {
	
	/*
	 * sends and receives data from server
	 */
	
	int port;
	InetAddress address;
	private Manager.listenerCBs cbs;
	private DatagramSocket client;
	
	public Listener(String address, int port, Manager.listenerCBs cbs){
		this.port = port;
		this.cbs = cbs;
		try {
			client = new DatagramSocket();
			this.address = InetAddress.getByName(address);
			
		} catch (Exception e) {}
		
	}
	
	public void send(String message){
		byte[] data = message.getBytes();
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		
		try{
			client.send(packet);
		} catch(Exception e){}
	}
	

	public void run(){
		send(CreateJson.Login(Consts.DISCONNECTED, "just for fun").toString());
		
		byte[] data = new byte[2024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		
		while(cbs.isLive()){
		    try{
		    	client.receive(packet);
		    	cbs.identifyPackage(new JSONObject(packet.getData().toString()));
		    
		    }catch(Exception e){}
		}
		
	}
}
