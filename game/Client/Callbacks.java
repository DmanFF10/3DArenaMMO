package Client;

import org.lwjgl.util.vector.Vector3f;

import GameLibrary.GameClient;
import GameLibrary.Thing;

 /* callbacks for the client to reference important game data */

public class Callbacks {

	// callback for the listener class
	public interface listenerCBs{
		boolean isLive();
		boolean isConnected();
		Thing initConnect();
		void identifyPackage(Thing data);
	}
	
	// callback for the visualizer class
	public interface visualizerCBs{
		void endLive();
		void requestMove(Vector3f direction, Vector3f rotation);
		Manager.State state();
		GameClient game();
	}
	
	
	
	
}
