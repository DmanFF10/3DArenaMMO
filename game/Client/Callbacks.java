package Client;

import org.json.JSONObject;

public class Callbacks {

	public interface listenerCBs{
		boolean isLive();
		void initConnect();
		void identifyPackage(JSONObject data);
	}
	
	public interface visualizerCBs{
		void endLive();
	}
	
	
	
	
}
