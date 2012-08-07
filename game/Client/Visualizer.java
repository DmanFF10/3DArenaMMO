package Client;

import org.lwjgl.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;

import GameLibrary.Consts;
import GameLibrary.Face;
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
	public float unitsize = 0.0002f;
	public float rotationSpeed = .0125f;

	//private UnicodeFont font;
	//private DecimalFormat format = new DecimalFormat("#.#####");

	public static enum State {
		Disconnected, Connected
	}
	private State state = State.Disconnected;

	// holds callbacks
	private Callbacks.visualizerCBs cbs;

	// display size
	private int width = 800;
	private int height = 600;

	// time
	private long lastFrame;
	private int delta;

	// the viewing object
	private Camera camera = new Camera();
	private Character player;
	private Map map;

	//inputs
	boolean keyUp, keyDown, keyLeft, keyRight, mouseLeft, changed;


	public Visualizer(Callbacks.visualizerCBs	cbs){
		this.cbs = cbs;
	}

	public void run() {
		initOpenGL();
		initCamera();
		lastFrame = getTime();
		while(!Display.isCloseRequested()){
			// renders the frames
			switch(state){
			case Disconnected:
				Display.update();
				break;

			case Connected:
				inputs();
				updateUnits();
				render();
				break;
			}
		}
		// clean up memory after closing display
		// end the program
		Display.destroy();
		cbs.endLive();
	}

	private void render() {
		// clear the screen
		glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();

		// sector and polygon translation variables
		Vector3f sloc;
		Vector3f ploc;
		Vector3f trans = new Vector3f(-camera.position.x, -camera.position.y, -camera.position.z);
		Vector3f rotate = new Vector3f(-camera.rotation.x, -camera.rotation.y, -camera.rotation.z);		

		/* draw the map */
		for (int snum = 0; snum<map.sectors.size(); snum++){
			Sector sector =  map.sectors.get(snum);
			sloc = sector.position;
			for (int onum = 0; onum<sector.objects.size(); onum++){
				Polygon object = sector.objects.get(onum);
				ploc = object.position;
				// translates and rotates instead of rotating or translating the camera
				// and draws the world
				glRotatef(rotate.x, 0f, 1f, 0f);
				glRotatef(rotate.y, 1f, 0f, 0f);
				glTranslatef(trans.x, trans.y, trans.z);
				glTranslatef(sloc.x, sloc.y, sloc.z);
				glTranslatef(ploc.x, ploc.y, ploc.z);
				// draws polygon

				glBegin(GL_POLYGON);
				for(Face face : object.faces){
					for (int i = 0; i<face.vertex.size(); i++){
						Vector3f normal = object.normals.get(face.normal.get(i) -1);
						Vector3f vertex = object.vertices.get(face.vertex.get(i)-1);
						glNormal3f(normal.x, normal.y, normal.z);
						glVertex3f(vertex.x, vertex.y, vertex.z);
					}
				}
				glEnd();

				// set position to the origin
				glLoadIdentity();
			}

		}

		/* draw players */
		for (int playerID = 0; playerID<cbs.game().playerSize(); playerID++){
			Character unit = cbs.game().getCharacter(playerID);
			glRotatef(rotate.x, 0f, 1f, 0f);
			glRotatef(rotate.y, 1f, 0f, 0f);
			glTranslatef(trans.x, trans.y, trans.z);
			glTranslatef(unit.object.position.x, unit.object.position.y, unit.object.position.z);
			glBegin(GL_POLYGON);
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

		// draws data to the screen
		Display.update();
		// sets the fps
		Display.sync(60);
	}

	private void updateUnits(){
		float xValue, zValue;
		int id = cbs.game().getID();
		for(int i=0; i<cbs.game().playerSize(); i++){
			Character unit = cbs.game().getCharacter(i);
			if (unit.movement.z == Consts.MOVE_BACKWORD_RIGHT){
				float angle = unit.object.rotation.x;
				float movement = (delta*(unit.speed * unitsize));
				xValue = (float) Math.sin(Math.toRadians(angle)) * movement;
				zValue = (float) Math.cos(Math.toRadians(angle)) * movement;
				unit.object.position.z += zValue;
				unit.object.position.x += xValue;
				if (i == id){
					camera.position.z += zValue;
					camera.position.x += xValue;
				}
			}

			if (unit.movement.z == Consts.MOVE_FORWORD_LEFT) {
				float angle = unit.object.rotation.x;
				float movement = (delta*(unit.speed * unitsize));
				xValue = (float) Math.sin(Math.toRadians(angle)) * movement;
				zValue = (float) Math.cos(Math.toRadians(angle)) * movement;
				unit.object.position.z -= zValue;
				unit.object.position.x -= xValue;
				if (i == id){
					camera.position.z -= zValue;
					camera.position.x -= xValue;
				}
			}

			if (unit.movement.x == Consts.MOVE_FORWORD_LEFT){
				float angle = unit.object.rotation.x;
				float movement = (delta*(unit.speed * unitsize));
				zValue = (float) Math.sin(Math.toRadians(angle)) * movement;
				xValue = (float) Math.cos(Math.toRadians(angle)) * movement;
				unit.object.position.z -= zValue;
				unit.object.position.x -= xValue;
				if (i == id){
					camera.position.z -= zValue;
					camera.position.x -= xValue;
				}
			}

			if (unit.movement.x == Consts.MOVE_BACKWORD_RIGHT){
				float angle = unit.object.rotation.x;
				float movement = (delta*(unit.speed * unitsize));
				zValue = (float) Math.sin(Math.toRadians(angle)) * movement;
				xValue = (float) Math.cos(Math.toRadians(angle)) * movement;
				unit.object.position.z += zValue;
				unit.object.position.x += xValue;
				if (i == id){
					camera.position.z += zValue;
					camera.position.x += xValue;
				}
			}
		}
	}

	private void inputs() {
		// get time between frames
		delta = getDelta();
		// get map data
		map = cbs.game().map;
		// get character data
		player = cbs.game().getCharacter(cbs.game().getID());
		// temp values
		float xValue, yValue;
		// stores the direction the player is moving
		Vector3f direction = new Vector3f();

		// stores old move
		boolean up = keyUp,
				down = keyDown,
				left = keyLeft,
				right = keyRight,
				mleft = mouseLeft;

		// if key change send request to server
		if (
				(keyUp = Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W)) != up ||
				(keyDown = Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S)) != down ||
				(keyLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A)) != left ||
				(keyRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D)) != right ||
				(mouseLeft = Mouse.isButtonDown(0)) != mleft
				) {
			changed = true;
		}

		if (mouseLeft){
			//TODO: make this go around the character object
			int x = Mouse.getDX();
			int y = Mouse.getDY();
			xValue = x*rotationSpeed;
			yValue = y*rotationSpeed;
			camera.rotation.x += xValue;
			camera.rotation.y += yValue;
			player.object.rotation.x += xValue;
			player.object.rotation.y += yValue;
		}

		if (keyDown){
			direction.z = Consts.MOVE_BACKWORD_RIGHT;
		}

		if (keyUp) {
			direction.z = Consts.MOVE_FORWORD_LEFT;
		}

		if (keyLeft){
			direction.x = Consts.MOVE_FORWORD_LEFT;
		}

		if (keyRight){
			direction.x = Consts.MOVE_BACKWORD_RIGHT;
		}
		// sends the move request to server
		if(changed){
			cbs.requestMove(direction, player.object.rotation);
			changed = false;
		}
	}

	public void setState(State state){
		this.state = state;
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

	private void initOpenGL(){
		try {
			// create the rendering window
			Display.setDisplayMode(new DisplayMode(width, height));
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
		gluPerspective(45.0f,(float)width/(float)height,0.1f,100.0f);

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

	private void initCamera(){
		camera.position.set(0f, 3f, 8f);

	}
}
