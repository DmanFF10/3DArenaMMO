package Client.Visualizer;


import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import org.lwjgl.opengl.*;

import de.matthiasmann.twl.GUI;

import Client.Manager.ICallbacks;
import Client.Manager.Manager;
import Client.Manager.Properties;
import Client.Visualizer.Interface.*;
import Client.Visualizer.util.Time;
import GameLibrary.*;

/*
 * displays the graphics
 */

public class Visualizer extends Thread {
	// properties
	private Properties properties = new Properties();

	// callbacks
	private ICallbacks.visualizerCBs cbs;

	// time
	private long lastFrame = Time.getTime();
	private int delta, fps, fpscount;

	private Manager.State state;
	
	// primary game objects
	//private Camera camera = new Camera();
	//private Character player;
	private GameClient game;
	//private Map map;

	//inputs
	boolean keyUp, keyDown, keyLeft, keyRight, mouseLeft, changed;
	private GUI gui;
	

	public Visualizer(ICallbacks.visualizerCBs	cbs){
		// set callbacks
		this.cbs = cbs;
	}

	public void run() {
		if(LWJGL.initOpenGL(properties.fullscreen, properties.width, properties.height)){
			// update all the variables for the first time
			updateVars();
			
			// main render loop
			while(!Display.isCloseRequested()){
				render();
			}
			
			// tell the server to log you out
			if (cbs.state() != Manager.State.Login){
				cbs.logout();
			}
			// clean up memory after closing display
			Display.destroy();
			// write property changes to file
			properties.writeProperties();
			// close the program
			cbs.endLive();
		}
	}

	/* Drawing Mechanics */
	private void render() {
		// clear the screen
		glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();
		
		// processes according to the state of the game
		switch(state){
			case Login:
				((Login)gui.getChild(0)).update(fps);
				break;
			
			case Lobby:
					ArrayList<String[]> chat = game.getChat();
					ArrayList<String> users = game.getUsernames();
					((Lobby)gui.getChild(0)).update(users, chat, fps);
				break;
			
			case Game:
				break;
		}
		
		//update gui
		LWJGL.enable2D();
		gui.update();
		LWJGL.disable2D();
		
		// draws data to the screen
		Display.update();
		// sets the fps
		Display.sync(60);
		
		// update variables
		updateVars();
	}
	
	/*private void drawTerrain(Vector3f trans, Vector3f rotate){
		Vector3f sloc, ploc;
		// loops through the sectors
		for (int snum = 0; snum<map.sectors.size(); snum++){
			// get each sectors objects and trans location 
			Sector sector =  map.sectors.get(snum);
			sloc = sector.position;
			
			// loops through the polygons
			for (int onum = 0; onum<sector.objects.size(); onum++){
				// get each objects trans location and vertices 
				Polygon object = sector.objects.get(onum);
				ploc = object.position;
				// translates and rotates instead of rotating or translating the camera
				// and draws the world
				glRotatef(rotate.x, 1f, 0f, 0f);
				glRotatef(rotate.y, 0f, 1f, 0f);
				glRotatef(rotate.z, 0f, 0f, 1f);
				glTranslatef(trans.x, trans.y, trans.z);
				glTranslatef(sloc.x, sloc.y, sloc.z);
				glTranslatef(ploc.x, ploc.y, ploc.z);
				glColor3f((float)(object.color.getRed()*Consts.COLOR_OFFSET), 
						(float)(object.color.getGreen()*Consts.COLOR_OFFSET), 
						(float)(object.color.getBlue()*Consts.COLOR_OFFSET));
				
				// draws each object
				glBegin(GL_QUADS);
				for(Face face : object.faces){
					for (int i = 0; i<face.vertex.size(); i++){
						Vector3f normal = object.normals.get(face.normal.get(i) -1);
						Vector3f vertex = object.vertices.get(face.vertex.get(i)-1);
						glNormal3f(normal.x, normal.y, normal.z);
						glVertex3f(vertex.x, vertex.y, vertex.z);
					}
				}
				glEnd();
				glLoadIdentity();
			}
		}
	}
	
	private void drawPlayers(Vector3f trans, Vector3f rotate){
		int playerSize = game.playerSize();	
		for (int playerID = 0; playerID<playerSize; playerID++){
			Character unit = game.getCharacter(playerID);
			glRotatef(rotate.x, 1f, 0f, 0f);
			glRotatef(rotate.y, 0f, 1f, 0f);
			glRotatef(rotate.z, 0f, 0f, 1f);
			glTranslatef(trans.x, trans.y, trans.z);
			glTranslatef(unit.object.position.x, unit.object.position.y, unit.object.position.z);
			glRotatef(unit.object.rotation.y, 0f, 1f, 0f);
			glColor3f((float)(unit.object.color.getRed()*Consts.COLOR_OFFSET), 
					(float)(unit.object.color.getGreen()*Consts.COLOR_OFFSET), 
					(float)(unit.object.color.getBlue()*Consts.COLOR_OFFSET));
			
			glBegin(GL_TRIANGLES);
			for(Face face : unit.object.faces){
				for (int i = 0; i<face.vertex.size(); i++){
					Vector3f normal = unit.object.normals.get(face.normal.get(i) -1);
					Vector3f vertex = unit.object.vertices.get(face.vertex.get(i)-1);
					glNormal3f(normal.x, normal.y, normal.z);
					glVertex3f(vertex.x, vertex.y, vertex.z);
				}
			}
			glEnd();				
			glLoadIdentity();
		}
	}
	
	private void processInputs() {
		// stores the direction the player is moving
		Vector3f direction = new Vector3f();
		// stores old move
		boolean up = keyUp, down = keyDown,
				left = keyLeft, right = keyRight,
				mleft = mouseLeft;

		// if key change send request to server
		if ((keyUp = Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W)) != up ||
			(keyDown = Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S)) != down ||
			(keyLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A)) != left ||
			(keyRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D)) != right ||
			(mouseLeft = Mouse.isButtonDown(0)) != mleft) {  changed = true;  }

		if (mouseLeft){
			int x = -Mouse.getDX();
			int y = -Mouse.getDY()/4;
			if (camera.rotation.y >= 360){
				camera.rotation.y = (camera.rotation.y - 360) + x;
				player.object.rotation.y = (player.object.rotation.y - 360) + x;
			} else if (camera.rotation.y <= -360){
				camera.rotation.y = (camera.rotation.y + 360) + x;
				player.object.rotation.y = (player.object.rotation.y + 360) + x;
			}else{
				camera.rotation.y += x;
				player.object.rotation.y += x;	
			}
			if ((camera.rotation.x + y) < 90 && (camera.rotation.x + y) > -90){
				camera.rotation.x += y;
				player.object.rotation.x += y;
			}
		}
		
		if (keyDown) { direction.z = Consts.MOVE_BACKWORD_RIGHT; }
		if (keyUp)   { direction.z = Consts.MOVE_FORWORD_LEFT;   }
		if (keyLeft) { direction.x = Consts.MOVE_FORWORD_LEFT;   }
		if (keyRight){ direction.x = Consts.MOVE_BACKWORD_RIGHT; }
		if (Keyboard.isKeyDown(Keyboard.KEY_F4)){
			try {
				Display.setDisplayMode(LWJGL.toggleFullscreen(properties.fullscreen = !properties.fullscreen));
				gluPerspective(45.0f,(float)Display.getWidth()/(float)Display.getHeight(),0.1f,100.0f);
			} catch (LWJGLException e) {
				Logger.log(Logger.ERROR, "Failed to toggle fullscreen");
			}
			
		}
		
		// sends the move request to server
		player.movement = direction;
		if(changed){
			cbs.requestMove(direction, player.object.rotation);
			changed = false;
		}
	}
	
	private void updateCamera(){
		// sets the camera at its position relative to the player object
		Vector3f position = player.object.position;
		camera.position.x = ((float) Math.sin(Math.toRadians(camera.rotation.y))*10)+position.x;
		camera.position.y = -((float) Math.sin(Math.toRadians(camera.rotation.x))*10)+position.y;
		camera.position.z = ((float) Math.cos(Math.toRadians(camera.rotation.y))*10)+position.z;
	}
*/

	private void updateVars(){

		// get time between frames
		delta = Time.getDelta(lastFrame);
		lastFrame = Time.getTime();
		// get game data
		game = cbs.game();
		//get game state
		state = cbs.state();
		
		if (fpscount >= Consts.TIME_SECOND){
			fps = Consts.TIME_SECOND/delta;
			fpscount = 0;
		}
		fpscount += delta;
		
		if (cbs.stateChanged()){
			loadmenus(state);
			cbs.stateSet();
		}
	}
	
	private void loadmenus(Manager.State menu){
		switch(menu){
			case Login:
				gui = Interface.createGUI(new Login(cbs, properties.debugMode), "Login");
				break;
			case Lobby:
				gui = Interface.createGUI(new Lobby(cbs, properties.debugMode), "Lobby");
				break;
			case Game:
				break;
		}
	}
}
