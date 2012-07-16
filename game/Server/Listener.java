package Server;

import java.net.DatagramSocket;

public class Listener {
	
	private DatagramSocket server;
	private Manager.listenerCBs cbs;
	
	public Listener(int port, Manager.listenerCBs cbs){
		System.out.println("Starting server...");
		this.cbs = cbs;
		try{
			server = new DatagramSocket(port);
			listen();
			
		} catch(Exception ex) {
			System.out.println("Error: port in use");
		}
	}
	
	private void listen(){
		while(cbs.isLive()){
			
		}
	}
	
}
