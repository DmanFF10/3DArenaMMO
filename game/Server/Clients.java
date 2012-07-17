package Server;

import java.net.InetAddress;

public class Clients {
	
	private InetAddress address;
	private int port;
	
	public Clients(InetAddress address, int port){
		this.address = address;
		this.port = port;
	}
	
	public InetAddress Address(){
		return address;
	}
	
	public int Port(){
		return port;
	}
}
