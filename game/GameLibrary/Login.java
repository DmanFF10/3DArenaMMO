package GameLibrary;

import java.io.Serializable;

public class Login extends Thing implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public Login(int id, String username){
		super(id, username, Consts.TYPE_LOGIN);
	}
}
