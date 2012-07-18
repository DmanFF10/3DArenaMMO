package GameLibrary;

import org.json.JSONObject;

/*
 * json object standards for each type of object
 */

public class CreateJson {

	public static JSONObject Login(int id, String username){
		JSONObject jobject = new JSONObject();
		jobject.put("id", id);
		jobject.put("username", username);
		jobject.put("type", "login");
		return jobject;
	}
	
	public static JSONObject Command(int id, String username){
		JSONObject jobject = new JSONObject();
		jobject.put("id", id);
		jobject.put("username", username);
		jobject.put("type", "command");
		return jobject;
	}
	
	public static JSONObject Message(int id, String username, String message){
		JSONObject jobject = new JSONObject();
		jobject.put("id", id);
		jobject.put("username", username);
		jobject.put("type", "message");
		jobject.put("message", message);
		return jobject;
		
	}
	
}
