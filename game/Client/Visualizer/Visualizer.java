package Client.Visualizer;

import java.io.File;
import java.io.IOException;

import org.lwjgl.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ListBox;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;

import Client.Manager.Callbacks;
import Client.Manager.Properties;
import Client.Manager.Callbacks.visualizerCBs;
import GameLibrary.*;
import GameLibrary.Character;
import GameLibrary.Graphics.Face;
import GameLibrary.Graphics.Polygon;
import GameLibrary.Graphics.Sector;
import GameLibrary.util.Consts;
import GameLibrary.util.Logger;
import static org.lwjgl.util.glu.GLU.gluPerspective;

/*
 * displays the graphics
 */

public class Visualizer extends Thread {
	// properties
	private Properties properties = new Properties();

	// callbacks
	private Callbacks.visualizerCBs cbs;

	// time
	private long lastFrame = getTime();
	private int delta, fps, fpscount;

	// primary game objects
	private Camera camera = new Camera();
	private Character player;
	private GameClient game;
	private Map map;

	//inputs
	boolean keyUp, keyDown, keyLeft, keyRight, mouseLeft, changed;
	int currentUI = -1;
	private GUI gui;
	

	public Visualizer(Callbacks.visualizerCBs	cbs){
		// set callbacks
		this.cbs = cbs;
	}

	public void run() {
		if(LWJGL.initOpenGL(properties.fullscreen, properties.width, properties.height)){
			// main render loop
			while(!Display.isCloseRequested()){
				render();
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
		switch(cbs.state()){
			case Login:
				// display menu
				if (currentUI != Consts.GUI_LOGIN){
					loadmenus(Consts.GUI_LOGIN);
					currentUI = Consts.GUI_LOGIN;
				}
				LWJGL.enable2D();
				gui.update();
				LWJGL.disable2D();
				break;
			
			case Lobby:
				if (currentUI != Consts.GUI_LOBBY){
					loadmenus(Consts.GUI_LOBBY);
					currentUI = Consts.GUI_LOBBY;
				}
				LWJGL.enable2D();
				gui.update();
				LWJGL.disable2D();
				break;
		
			/*case Game:
				updateVars();
				processInputs();
				game.updateUnits(delta);
				updateCamera();
				
				// view translation and rotations
				Vector3f trans = new Vector3f(-camera.position.x, -camera.position.y, -camera.position.z);
				Vector3f rotate = new Vector3f(-camera.rotation.x, -camera.rotation.y, -camera.rotation.z);
		
				drawTerrain(trans, rotate);
				drawPlayers(trans, rotate);
				break;*/
		}
		
		// draws data to the screen
		Display.update();
		// sets the fps
		Display.sync(60);
	}
	
	private void drawTerrain(Vector3f trans, Vector3f rotate){
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
	
	/*private void drawPlayers(Vector3f trans, Vector3f rotate){
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
	}*/
	
	/* Updating and Calculations */
	private long getTime(){
		// return the current time
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	private int getDelta(){
		// returns the time between frames (delta)
		long currentTime = getTime();
		int delta = (int) (currentTime - lastFrame);
		lastFrame = getTime();
		return delta;
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
	
	/*private void updateVars(){

		// get time between frames
		delta = getDelta();
		// get game data
		game = cbs.game();
		// get map data
		map = game.map;
		// get character data
		player = game.getCharacter(game.getID());
		
		if (fpscount >= 1000){
			fps = 1000/delta;
			fpscount = 0;
		}
		fpscount += delta;
	}*/
	
	public void loadmenus(int menu){
		switch(menu){
			case Consts.GUI_LOGIN:
				gui = initTWL(login());
				break;
			case Consts.GUI_LOBBY:
				gui = initTWL(lobby());
		}
	}
	
	
	private GUI initTWL(Widget w){
		try{
			GUI g = new GUI(w, new LWJGLRenderer());
			g.applyTheme(ThemeManager.createThemeManager(
					(new File("./game/res/menus/mainTemplate.xml")).toURI().toURL(), new LWJGLRenderer()));
			return g;
		} catch(Exception e){
			Logger.log(Logger.ERROR, "failed to load TWL");
			return null;
		}
	}
	
	private Widget login() {
		// display size
		int height = Display.getHeight()/2;
		int width = Display.getWidth()/2;
		// each objects sizes
		int objWidth, objHeight;
		
		Widget w = new Widget();
		final Button loginButton = new Button("Login");
		final EditField nameTextBox = new EditField();
		final EditField passTextBox = new EditField();
		final Label nameLabel = new Label("UserName:");
		final Label passLabel = new Label("Password:");
		
		// buttons
		objWidth = 65;
		objHeight = 30;
		loginButton.setTheme("button");
		loginButton.setSize(objWidth, objHeight);
		loginButton.setPosition(width-(objWidth/2), height+(objHeight*2));
		
		// textboxes
		objWidth = 150;
		objHeight = 20;
		nameTextBox.setTheme("textBox");
		nameTextBox.setSize(objWidth, objHeight);
		nameTextBox.setPosition(width-(objWidth/2), (int)(height-(objHeight*1.5)));
		
		passTextBox.setTheme("textBox");
		passTextBox.setSize(objWidth, objHeight);
		passTextBox.setPosition(width-(objWidth/2), height);
		
		// labels
		objWidth = 5*nameLabel.getText().length();
		nameLabel.setTheme("label");
		nameLabel.setPosition(nameTextBox.getX()-(objWidth+nameTextBox.getWidth()/4),
		nameTextBox.getY()+(nameTextBox.getHeight()/2));
		
		objWidth = 5*passLabel.getText().length();
		passLabel.setTheme("label");
		passLabel.setPosition(passTextBox.getX()-(objWidth+(passTextBox.getWidth()/4)),
		passTextBox.getY()+(passTextBox.getHeight()/2));
		
		// event listeners
		loginButton.addCallback(new Runnable() {
		            public void run() {
		                cbs.connect(nameTextBox.getText(), passTextBox.getText());
		            }
		        });
		
		w.add(nameLabel);
		w.add(passLabel);
		w.add(loginButton);
		w.add(nameTextBox);
		w.add(passTextBox);
		return w;
	}
	
	private Widget lobby() {
		// display size
		int height = Display.getHeight();
		int width = Display.getWidth();
		// each objects sizes
		int objWidth, objHeight;
		
		Widget w = new Widget();
		final Button logoutButton = new Button("Logout");
		final Button settingsButton = new Button("Settings");
		final Button profileButton = new Button("Profile");
		final Button storeButton = new Button("Store");
		final Button newsButton = new Button("News");
		final Button playButton = new Button("Play");
		
		final ListBox playersListBox = new ListBox();
		
		//buttons
		objWidth = 65;
		objHeight = 30;
		logoutButton.setTheme("button");
		logoutButton.setSize(objWidth, objHeight);
		logoutButton.setPosition(width-(objWidth+15), 0);
		
		settingsButton.setTheme("button");
		settingsButton.setSize(objWidth, objHeight);
		settingsButton.setPosition(width-((objWidth+15)*2), 0);
		
		profileButton.setTheme("button");
		profileButton.setSize(objWidth, objHeight);
		profileButton.setPosition(width-((objWidth+15)*3), 0);
		
		storeButton.setTheme("button");
		storeButton.setSize(objWidth, objHeight);
		storeButton.setPosition(width-((objWidth+15)*4), 0);
		
		newsButton.setTheme("button");
		newsButton.setSize(objWidth, objHeight);
		newsButton.setPosition(width-((objWidth+15)*5), 0);
		
		playButton.setTheme("button");
		playButton.setSize(objWidth, objHeight);
		playButton.setPosition(width-((objWidth+15)*6), 0);
		
		//list boxes
		objWidth = width/4;
		objHeight = height-45;
		playersListBox.setTheme("listbox");
		playersListBox.setSize(objWidth, objHeight);
		playersListBox.setPosition(width-(objWidth+15), 35);
		
		
		
		w.add(logoutButton);
		w.add(settingsButton);
		w.add(profileButton);
		w.add(storeButton);
		w.add(newsButton);
		w.add(playButton);
		w.add(playersListBox);
		return w;
	}
}
