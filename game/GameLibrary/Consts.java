package GameLibrary;

/*
 * constants for the game
 */

public class Consts {
	public static final int DISCONNECTED = -1;
	
	public static final int TYPE_LOGIN = 0;
	public static final int TYPE_LOGOUT = 1;
	public static final int TYPE_MOVE = 2;
	public static final int TYPE_MESSAGE = 3;
	public static final int TYPE_MAP = 5;
	public static final int TYPE_LOGIN_PASS = 6;
	public static final int TYPE_LOGIN_FAIL = 7;
	
	public static final int MESSAGE_GROUP = 0;
	public static final int MESSAGE_ALL = 1;
	
	public static final int MOVE_STOP = 0;
	public static final int MOVE_FORWORD_LEFT = -1;
	public static final int MOVE_BACKWORD_RIGHT = 1;
	
	public static final int GUI_LOGIN = 0;
	public static final int GUI_LOBBY = 1;
	
	public static final String PACK_BEGIN = "BEGIN";
	public static final String PACK_END = "END";
	public static final String PACK_SPLITER = "~";
	
	public static final float UNITSIZE = 0.0002f;
	public static final float COLOR_OFFSET = 0.00390625f;
}
