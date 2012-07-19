package GameLibrary;

import java.util.ArrayList;

import org.json.JSONObject;

/*
 * json object standards for each type of object
 */

public class CreateJson {

	public static JSONObject Login(int id, String username){
		JSONObject jobject = new JSONObject();
		jobject.put("id", id);
		jobject.put("username", username);
		jobject.put("type", Consts.TYPE_LOGIN);
		return jobject;
	}
	
	public static JSONObject Login(int id, String username, ArrayList<Character> units, Map map){
		JSONObject jobject = new JSONObject();
		jobject.put("id", id);
		jobject.put("username", username);
		jobject.put("type", Consts.TYPE_LOGIN);
		jobject.put("characters", units);
		jobject.put("map", map);
		return jobject;
	}
	
	public static JSONObject Move(int id, String username, int[] location){
		JSONObject jobject = new JSONObject();
		jobject.put("id", id);
		jobject.put("username", username);
		jobject.put("type", Consts.TYPE_MOVE);
		jobject.put("location", location);
		return jobject;
	}
	
	public static JSONObject Message(int id, String username, String message, int style, int[] players){
		JSONObject jobject = new JSONObject();
		jobject.put("id", id);
		jobject.put("username", username);
		jobject.put("type", Consts.TYPE_MESSAGE);
		jobject.put("message", message);
		jobject.put("style", style);
		jobject.put("players", players);
		return jobject;
		
	}
	
}
