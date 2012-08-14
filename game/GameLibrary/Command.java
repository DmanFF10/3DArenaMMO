package GameLibrary;

import java.io.Serializable;

import org.lwjgl.util.vector.Vector3f;

public class Command extends Thing implements Serializable {
	private static final long serialVersionUID = 1L;
	private Object obj;
	private int messageType;
	private Vector3f movement, rotation, position;
	
	public Command(int id, String username, String message, int messageType){
		super(id, username, Consts.TYPE_MESSAGE);
		this.obj = message;
		this.messageType = messageType;
	}
	
	public Command(int id, String username, Character player){
		super(id, username, Consts.TYPE_NEW_PlAYER);
		this.obj = player;
	}
	
	public Command(int id, String username, Vector3f movement, Vector3f rotation){
		super(id, username, Consts.TYPE_MOVE);
		this.movement = movement;
		this.rotation = rotation;
	}
	
	public Command(int id, String username, Vector3f position, Vector3f movement, Vector3f rotation){
		super(id, username, Consts.TYPE_MOVE);
		this.position = position;
		this.movement = movement;
		this.rotation = rotation;
	}
	
	public Command(int id, String username, String mappart){
		super(id, username, Consts.TYPE_MAP);
		this.obj = mappart;
	}
	
	public Object getObject(){
		return obj;
	}
	
	public int getMessageType(){
		return messageType;
	}
	
	public Vector3f getMovement(){
		return movement;
	}
	
	public Vector3f getRotation(){
		return rotation;
	}
	
	public Vector3f getPosition(){
		return position;
	}
}
