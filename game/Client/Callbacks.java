package Client;

import org.json.JSONObject;
import GameLibrary.Map;

public class Callbacks {

	public interface listenerCBs{
		boolean isLive();
		void initConnect();
		void identifyPackage(JSONObject data);
	}
	
	public interface visualizerCBs{
		void endLive();
		Map map();
	}
	
	
	
	
}
