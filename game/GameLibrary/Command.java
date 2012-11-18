package GameLibrary;
import java.util.ArrayList;

import GameLibrary.util.Consts;
import GameLibrary.util.Logger;

public class Command {
	private int id, commandType;
	private String username, password;
	
	public Command(int id){
		this.id = id;
		this.commandType = Consts.TYPE_LOGOUT;
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
	
	public String getUsername(){
		return username;
	}
	
	public String getPassword(){
		return password;
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
				
			
			case Consts.TYPE_LOGIN_PASS:
				data.add(begin());
				data.add(type());
				data.add(id() + username);
				data.add(end());
				break;
		}
		return data;
	}
	
	public static Command unpack(ArrayList<String> data){
		Command cmd = null;
		int id, type;
		try{
			if (split(data.get(0))[1].equals(Consts.PACK_BEGIN)){
				type = Integer.parseInt(split(data.get(1))[1]);
				switch(type){
					case Consts.TYPE_LOGIN_PASS:
						String[] s = split(data.get(2));
						id = Integer.parseInt(s[0]);
						String username = s[1];
						cmd = new Command(id, username);
						break;
					
					case Consts.TYPE_LOGOUT:
						id = Integer.parseInt(split(data.get(2))[0]);
						cmd = new Command(id);
						break;
				}
			}
		}catch(Exception e){
			Logger.log(Logger.WORNING, "could not unpack the command");
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
		return id + Consts.PACK_SPLITER + Consts.TYPE_LOGIN_PASS;
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
			for(int i = 0; i<value.length; i++){
				value[i] = value[i].trim();
			}
		return value;
	}
}
