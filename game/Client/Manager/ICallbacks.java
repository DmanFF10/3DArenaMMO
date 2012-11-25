package Client.Manager;

import org.lwjgl.util.vector.Vector3f;

import GameLibrary.GameClient;

 /* callbacks for the client to reference important game data */

public class ICallbacks {

	// callback for the listener class
	public interface listenerCBs{
		boolean isLive();
		boolean isConnected();
		void process(String s);
	}
	
	// callback for the visualizer class
	public interface visualizerCBs{
		void endLive();
		boolean updated();
		void finishedUpdating();
		void connect(String name, String password);
		void logout();
		void sendChat(String message);
		void requestMove(Vector3f direction, Vector3f rotation);
		Manager.State state();
		GameClient game();
	}
	
	
	
	
}
