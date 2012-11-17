package GameLibrary.util;

import java.util.ArrayList;
import GameLibrary.Command;

public class Packer {
	
	public static ArrayList<String> pack(Command cmd){
		ArrayList<String> data = new ArrayList<String>();
		int id = cmd.getID();
		
		switch(cmd.getCommandType()){
			case Consts.TYPE_LOGIN:
				data.add(id + Consts.PACK_SPLITER + cmd.getUsername() + Consts.PACK_SPLITER + cmd.getPassword());
				break;
			
			case Consts.TYPE_LOGIN_PASS:
				data.add(id + Consts.PACK_SPLITER + Consts.PACK_BEGIN);
				data.add(id + Consts.PACK_SPLITER + Consts.TYPE_LOGIN_PASS);
				data.add(id + Consts.PACK_SPLITER + cmd.getUsername());
				data.add(id + Consts.PACK_SPLITER + Consts.PACK_END);
				break;
		}
		return data;
	}
	
	public static Command unpack(ArrayList<String> data){
		Command cmd = null;
		try{
			if (split(data.get(0))[1].equals(Consts.PACK_BEGIN)){
				int type = Integer.parseInt(split(data.get(1))[1]);
				switch(type){
					case Consts.TYPE_LOGIN_PASS:
						String[] s = split(data.get(2));
						int id = Integer.parseInt(s[0]);
						String username = s[1];
						cmd = new Command(id, username);
						break;
				}
			}
		}catch(Exception e){
			Logger.log(Logger.WORNING, "could not unpack the command");
		}
		
		return cmd;
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
