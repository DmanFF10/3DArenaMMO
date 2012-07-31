package Client;

import org.lwjgl.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;

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
	public final float UNITSIZE = 0.00001f;
	public final int ROTSPEED = 5;
	
	/* public declarations*/ 
	public static enum State {
		Disconnected, Connected
	}
	
	/* private declarations*/
	// hold the callbacks
	private Callbacks.visualizerCBs cbs;
	// height and width of the display
	private int width = 800;
	private int height = 600;
	private State state = State.Disconnected;
	private long lastFrame;
	private int delta;
	// the viewing object
	private Camera camera = new Camera();
	private Map map;
	private Character player;
	
	/* Constructor(s) */
	public Visualizer(Callbacks.visualizerCBs	cbs){
		this.cbs = cbs;	
	}
	
	/* public methods */
	public void setState(State state){
		this.state = state;
	}

	public void run() {
		initOpenGL();
		lastFrame = getTime();
		while(!Display.isCloseRequested()){
			// renders the frames
			switch(state){
				case Disconnected:
					Display.update();
					break;
				
				case Connected:
					inputs();
					render();
					break;
			}
		}
		// clean up memory after closing display
		// end the program
		Display.destroy();
		cbs.endLive();
	}
	
	/* private methods */
	
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
	
	private void render() {
		// clear the screen
		glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();
		
		// sector and polygon translation variables
		Vector3f sloc;
		Vector3f ploc;
		
		float tranx = -camera.X;
		float trany = -camera.Y;
		float tranz = -camera.Z;
		float rotateX = -camera.Xrot;
		float rotateY = -camera.Yrot;		

		/* draw the map */
		for (int snum = 0; snum<map.sectors.size(); snum++){
			Sector sector =  map.sectors.get(snum);
			sloc = sector.tranloc;
			for (int onum = 0; onum<sector.objects.size(); onum++){
				Polygon object = sector.objects.get(onum);
				ploc = object.tranloc;
				// translates and rotates instead of rotating or translating the camera
				// and draws the world
				glRotatef(rotateX, 0f, 1f, 0f);
				glRotatef(rotateY, 1f, 0f, 0f);
				glTranslatef(tranx, trany, tranz);
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
		// draws data to the screen
		Display.update();
		// sets the fps
		Display.sync(60);
	}
	
	private void inputs() {
		delta = getDelta();
		map = cbs.game().map;
		player = cbs.game().getCharacter(cbs.game().getID());
		
		if (Mouse.isButtonDown(0)){
			int x = Mouse.getDX();
			int y = Mouse.getDY();
			camera.Xrot += x/ROTSPEED;
			camera.Yrot += y/ROTSPEED;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			float angle = camera.Xrot;
			float movement = (delta*(player.speed * UNITSIZE));
			camera.Z += (float) Math.cos(Math.toRadians(angle)) * movement;
			camera.X += (float) Math.sin(Math.toRadians(angle)) * movement;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			float angle = camera.Xrot;
			float movement = (delta*(player.speed * UNITSIZE));
			camera.Z -= (float) Math.cos(Math.toRadians(angle)) * movement;
			camera.X -= (float) Math.sin(Math.toRadians(angle)) * movement;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
			float angle = camera.Xrot;
			float movement = (delta*(player.speed * UNITSIZE));
			camera.Z -= (float) Math.sin(Math.toRadians(angle)) * movement;
			camera.X -= (float) Math.cos(Math.toRadians(angle)) * movement;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
			float angle = camera.Xrot;
			float movement = (delta*(player.speed * UNITSIZE));
			camera.Z += (float) Math.sin(Math.toRadians(angle)) * movement;
			camera.X += (float) Math.cos(Math.toRadians(angle)) * movement;
		}
	}
	
	/* Initialization Code */
	
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
}
