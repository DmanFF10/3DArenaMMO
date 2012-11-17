package GameLibrary;
import GameLibrary.util.Consts;

public class Command {
	private int id, commandType;
	private String username, password;
	
	public Command(int id,  String username, String password){
		this.id = id;
		this.username = username;
		this.commandType = Consts.TYPE_LOGIN;
	}
	
	public Command(int id, String username){
		this.id = id;
		this.username = username;
		this.commandType = Consts.TYPE_LOGIN_PASS;
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
}
