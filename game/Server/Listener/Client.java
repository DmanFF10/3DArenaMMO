package Server.Listener;

import java.net.InetAddress;

public class Client{
	
	private InetAddress address;
	private int port;
	
	public Client(InetAddress address, int port){
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
