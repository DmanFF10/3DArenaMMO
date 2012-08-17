package Client;

import java.awt.Font;

import org.lwjgl.*;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import GameLibrary.Consts;
import GameLibrary.Face;
import GameLibrary.GameClient;
import GameLibrary.Logger;
import GameLibrary.Map;
import GameLibrary.Polygon;
import GameLibrary.Sector;
import GameLibrary.Character;
import static org.lwjgl.util.glu.GLU.gluPerspective;

/*
 * displays the graphics
 */

public class Visualizer extends Thread {
	
	private Properties properties = new Properties();

	// holds callbacks
	private Callbacks.visualizerCBs cbs;

	// time
	private long lastFrame = getTime();
	private int delta, fps, fpscount;

	// the viewing object
	private Camera camera = new Camera();
	private Character player;
	private GameClient game;
	private Map map;

	//inputs
	boolean keyUp, keyDown, keyLeft, keyRight, mouseLeft, changed;

	// font
	private UnicodeFont text;

	public Visualizer(Callbacks.visualizerCBs	cbs){
		// set callbacks
		this.cbs = cbs;
	}

	public void run() {
		initOpenGL();
		initFonts();
		while(!Display.isCloseRequested()){
			// renders the frames
			switch(cbs.state()){
			case Disconnected:
				Display.update();
				break;

			case Connected:
				updateVars();
				getInputs();
				game.updateUnits(delta);
				updateCamera();
				render();
				break;
			}
		}
		// clean up memory after closing display
		Display.destroy();
		// disconnect from server
		cbs.disconnect();
		// write property changes to file
		properties.writeProperties();
		// close the program
		cbs.endLive();
	}

	private void render() {
		// clear the screen
		glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();
		
		// view translation and rotations
		Vector3f trans = new Vector3f(-camera.position.x, -camera.position.y, -camera.position.z);
		Vector3f rotate = new Vector3f(-camera.rotation.x, -camera.rotation.y, -camera.rotation.z);

		drawTerrain(trans, rotate);
		drawPlayers(trans, rotate);
		drawStatusText();
		
		// draws data to the screen
		Display.update();
		// sets the fps
		Display.sync(60);
	}
	
	private void drawStatusText(){
		enable2D();
		text.drawString(10, 15, "fps: " + fps);
		text.drawString(10, 30, "X: " + player.object.position.x);
		text.drawString(10, 45, "Y: " + player.object.position.y);
		text.drawString(10, 60, "Z: " + player.object.position.z);
		text.drawString(10, 75, "Angle X: " + player.object.rotation.x);
		text.drawString(10, 90, "Angle Y: " + player.object.rotation.y);
		disable2D();
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

	private void getInputs() {
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
				Display.setDisplayMode(toggleFullscreen(!properties.fullscreen));
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
	
	
	private void updateCamera(){
		// sets the camera at its position relative to the player object
		Vector3f position = player.object.position;
		camera.position.x = ((float) Math.sin(Math.toRadians(camera.rotation.y))*10)+position.x;
		camera.position.y = -((float) Math.sin(Math.toRadians(camera.rotation.x))*10)+position.y;
		camera.position.z = ((float) Math.cos(Math.toRadians(camera.rotation.y))*10)+position.z;
	}
	
	private void updateVars(){
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
	}

	private void enable2D(){
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, properties.width, properties.height, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}
	
	private void disable2D(){
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(45.0f,(float)Display.getWidth()/(float)Display.getHeight(),0.1f,100.0f);
		glMatrixMode(GL11.GL_MODELVIEW);
		glLoadIdentity();
	}
	
	private DisplayMode toggleFullscreen(boolean fullscreen) throws LWJGLException{
		properties.fullscreen = fullscreen;
		if (fullscreen){
			DisplayMode[] modes = Display.getAvailableDisplayModes();
			Display.setFullscreen(true);
			return modes[0];

			
		} else {
			Display.setFullscreen(false);
			return new DisplayMode(properties.width, properties.height);
		}
	}
	
	private void initOpenGL(){
		try {
			// create the rendering window
			Display.setDisplayMode(toggleFullscreen(properties.fullscreen));
			Display.setTitle("Arena Graphics Test");
			Display.create();
		} catch (LWJGLException e) {
			// log error and end the program
			Logger.log(Logger.ERROR, "Graphics window failed to load");
			cbs.endLive();
		}

		// switch mode to set up projection type
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();

		// sets a simple prospective
		gluPerspective(45.0f,(float)Display.getWidth()/(float)Display.getHeight(),0.1f,100.0f);

		// switch view back
		glMatrixMode(GL11.GL_MODELVIEW);
		glLoadIdentity();

		// set smooth shading
		glShadeModel(GL_SMOOTH);
		// set clear color to black
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// depth testing
		glClearDepth(1.0f);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);

		// quality of prospective calculations
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
	}
	
	@SuppressWarnings("unchecked")
	private void initFonts(){
		try {
			text = new UnicodeFont(new Font("Times New Roman", Font.PLAIN, 12));
			text.addAsciiGlyphs();
			text.getEffects().add(new ColorEffect(java.awt.Color.white));
			text.loadGlyphs();
		} catch (SlickException e) {}
	}
}
