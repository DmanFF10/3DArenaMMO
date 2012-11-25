package GameLibrary;
import java.util.ArrayList;

import GameLibrary.util.Logger;

public class Command {
	private int id, commandType, messageType;
	private String username, password, message;
	
	public Command(int id){
		this.id = id;
		this.commandType = Consts.TYPE_LOGOUT;
	}
	
	public Command(int id, int messageType, String message){
		this.id = id;
		this.commandType = Consts.TYPE_MESSAGE;
		this.messageType = messageType;
		this.message = message;
	}
	
	public Command(int id, String username){
		this.id = id;
		this.username = username;
		this.commandType = Consts.TYPE_LOGIN_PASS;
	}
	
	public Command(int id,  String username, String password){
		this.id = id;
		this.username = username;
		this.commandType = Consts.TYPE_LOGIN;
	}
	
	public int getID(){
		return id;
	}
	
	public int getCommandType() {
		return commandType;
	}
	
	public int getMessageType(){
		return messageType;
	}
	
	public String getUsername(){
		return username;
	}
	
	public String getPassword(){
		return password;
	}
	
	public String getMessage(){
		return message;
	}
	
	public ArrayList<String> pack(){
		ArrayList<String> data = new ArrayList<String>();
		
		switch(commandType){
			case Consts.TYPE_LOGIN:
				data.add(id() + username + Consts.PACK_SPLITER + password);
				break;
			
			case Consts.TYPE_LOGOUT:
				data.add(begin());
				data.add(type());
				data.add(end());
				break;
				
			
			case Consts.TYPE_LOGIN_PASS:
				data.add(begin());
				data.add(type());
				data.add(id() + username);
				data.add(end());
				break;
				
			case Consts.TYPE_MESSAGE:
				data.add(begin());
				data.add(type());
				data.add(id() + messageType);
				data.add(id() + message);
				data.add(end());
				break;
		}
		return data;
	}
	
	public static Command unpack(ArrayList<String> data){
		Command cmd = null;
		int id, type, messageType;
		try{
			if (split(data.get(0))[1].equals(Consts.PACK_BEGIN)){
				type = Integer.parseInt(split(data.get(1))[1]);
				switch(type){
					case Consts.TYPE_LOGIN_PASS:
						if (data.size() != 4)
							throw new Exception();
						String[] s = split(data.get(2));
						id = Integer.parseInt(s[0]);
						String username = s[1];
						cmd = new Command(id, username);
						break;
					
					case Consts.TYPE_MESSAGE:
						if (data.size() != 5)
							throw new Exception();
						String[] mtype = split(data.get(2));
						String message = split(data.get(3))[1];
						id = Integer.parseInt(mtype[0]);
						messageType = Integer.parseInt(mtype[1]);
						cmd = new Command(id, messageType, message);
						break;
						
					case Consts.TYPE_LOGOUT:
						if (data.size() != 3)
							throw new Exception();
						id = Integer.parseInt(split(data.get(2))[0]);
						cmd = new Command(id);
						break;
				}
			}
		}catch(Exception e){
			Logger.log(Logger.WORNING, "could not unpack the command  DATA: "+ data.toString());
		}
		return cmd;
	}
	
	private String id(){
		return id + Consts.PACK_SPLITER;
	}
	
	private String begin(){
		return id + Consts.PACK_SPLITER + Consts.PACK_BEGIN;
	}
	
	private String type(){
		return id + Consts.PACK_SPLITER + getCommandType();
	}
	
	private String end(){
		return id + Consts.PACK_SPLITER + Consts.PACK_END;
	}
	
	public static boolean isStart(String value){
		return split(value)[1].equals(Consts.PACK_BEGIN);
	}
	
	public static boolean isEnd(String value){
		return split(value)[1].equals(Consts.PACK_END);
	}
	
	private static String[] split(String s){
		String[] value = s.split(Consts.PACK_SPLITER);
		return value;
	}
}
