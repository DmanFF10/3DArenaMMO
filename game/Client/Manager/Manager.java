package Client.Manager;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import Client.Listener.Listener;
import Client.Manager.Callbacks.*;
import Client.Visualizer.Visualizer;
import GameLibrary.*;
import GameLibrary.util.*;

public class Manager {
	

	public static enum State {
		Login, Lobby, Game
	}
	
	private State state = State.Login;
	
	private boolean live = true;
	private int port = 1234;
	private String address = "127.0.0.1";
	private GameClient game;
	private Listener sender;
	private Visualizer view;
	private ArrayList<String> command = new ArrayList<String>();
	
	public Manager(){
		// stars logging
		Logger.startLogger("Client");
		startVisualizer();
		startGame();
		startListener();
	}
	
	public void startVisualizer(){
		// initialize the game view
		view = new Visualizer(vizCBs());
		Logger.log(Logger.INFO, "Starting graphical front end");
		view.start();
	}
	
	private void startListener(){
		sender = new Listener(address, port, initCBs());
		Logger.log(Logger.INFO, "Starting listener");
		sender.start();
	}
	
	private void startGame(){
		// Initialize game data container
		Logger.log(Logger.INFO, "Setting up game");
		game = new GameClient();
	}
	
	public void processCommand(Command data){
	}
	
	private listenerCBs initCBs(){
		// returns the callback functions for the listener object 
		return new listenerCBs() {
			
			// returns the state of the program
			public boolean isLive() {
				return live;
			} 
			
			public boolean isConnected() {
				return game.getID() != -1;
			}
			
			public void process(String data){
				Logger.log(Logger.DEBUG, data);
				if (Packer.isEnd(data)){
					Logger.log(Logger.DEBUG, "passed");
					command.add(data);
					processCommand(Packer.unpack(command));
					command = new ArrayList<String>();
				} else {
					command.add(data);
				}
			}
		};
	}
	
	
	
	private visualizerCBs vizCBs(){
		// returns the callback functions for the visualizer object
		return new visualizerCBs(){
			// sets the state of the program to false
			public void endLive(){
				live = false;
			}
			
			public void connect(String username, String password) {
				game.setName(username);
				Command login = new Command(game.getID(), username, password);
				sender.send(Packer.pack(login));
			}
			
			public void requestMove(Vector3f direction, Vector3f rotation){
			}
			
			public State state(){
				return state;
			}
			
			// returns the game object
			public GameClient game(){
				return game;
			}
		};
	}
}
