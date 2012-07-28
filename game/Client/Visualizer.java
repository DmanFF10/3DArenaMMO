package Client;

import org.lwjgl.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;

import GameLibrary.Map;
import GameLibrary.Polygon;
import GameLibrary.Sector;
import GameLibrary.Vertex;
import static org.lwjgl.util.glu.GLU.gluPerspective;


/*
 * displays the graphics
 */

public class Visualizer extends Thread {
	// hold the callbacks
	private Callbacks.visualizerCBs cbs;
	// height and width of the display
	private int width;
	private int height;

	// settings
	private boolean viewFPS;
	// the viewing object
	private Camera camera = new Camera();
	
	public Visualizer(Callbacks.visualizerCBs	cbs){
		// store the callbacks
		this.cbs = cbs;
		height = 600;
		width = 800;
		viewFPS = true;
	}

	public void run() {
		if (initDisplay()){
			initOpenGL();
			while(!Display.isCloseRequested()){
				// renders the frames
				inputs();
				render();
			}
			// when program is closing tell the main loop to end
			cbs.endLive();
			// do clean up
			Display.destroy();
		} else{
			// if fails to load display print error and tell
			// main loop to end
			System.out.println("Error: graphical issue");
			cbs.endLive();
		}
	}
	
	/* Display Operations */
	
	private void render() {
		// clear the screen
		glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();
		
		float tranx = -camera.X;
		float trany = -camera.Y;
		float tranz = -camera.Z;
		float rotateX = 360.0f - camera.Xrot;
		float rotateY = 360.0f - camera.Yrot;
		// draw map
		//NOTE: i dont like this. i will most likely change it
		Map map = cbs.map();
		glRotatef(rotateX, 0f, 1f, 0f);
		glRotatef(rotateY, 1f, 0f, 0f);
		glTranslatef(tranx, trany, tranz);
		glTranslatef(tranx, trany, tranz);
		for (int snum = 0; snum<map.size(); snum++){
			Sector sector =  map.getSector(snum);
			for (int onum = 0; onum<sector.size(); onum++){
				Polygon object = sector.getPolygon(onum);
				glBegin(GL_POLYGON);
				for(int vnum = 0; vnum<object.size(); vnum++){
					Vertex vertex = object.getVertex(vnum);
					glVertex3d(vertex.X, vertex.Y, vertex.Z);
				}
				glEnd();
			}
		}
		
		// set position to the origin
		glLoadIdentity();
		
		// draws data to the screen
		Display.update();
		// sets the fps
		Display.sync(60);
	}
	
	private void inputs() {
		if (Mouse.isButtonDown(0)){
			int x = Mouse.getDX();
			int y = Mouse.getDY();
			camera.Xrot += x/5;
			camera.Yrot += y/5;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			camera.Z += 0.1f;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			camera.Z -= 0.1f;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
			camera.X -= 0.1f;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
			camera.X += 0.1f;
		}
	}
	
	/* Initialization Code */
	
	private boolean initDisplay(){
		try {
			// create the rendering window
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle("Arena Graphics Test");
			Display.create();
		} catch (LWJGLException e) {
			return false;
		}
		return true;
	}
	
	private void initOpenGL(){
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
