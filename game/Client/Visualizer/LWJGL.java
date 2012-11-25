package Client.Visualizer;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import GameLibrary.util.Logger;

public class LWJGL {

	private static int HEIGHT, WIDTH;
	
	public static boolean initOpenGL(boolean fullscreen, int width, int height){
		WIDTH = width;
		HEIGHT = height;
		try {
			// create the rendering window
			Display.setDisplayMode(toggleFullscreen(fullscreen));
			Display.setTitle("Epic Online");
			Display.create();
		} catch (LWJGLException e) {
			// log error and end the program
			Logger.log(Logger.ERROR, "Graphics window failed to load");
			return false;
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
		return true;
	}
	
	public static DisplayMode toggleFullscreen(boolean fullscreen) throws LWJGLException{
		// TODO: make this adjust to user defined parameters
		if (fullscreen){
			DisplayMode[] modes = Display.getAvailableDisplayModes();
			Display.setFullscreen(true);
			return modes[0];

			
		} else {
			Display.setFullscreen(false);
			return new DisplayMode(WIDTH, HEIGHT);
		}
	}
	
	public static void enable2D(){
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glClearColor(0, 0, 0, 1);
		glLoadIdentity();
	}
	
	public static void disable2D(){
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(45.0f,(float)Display.getWidth()/(float)Display.getHeight(),0.1f,100.0f);
		glMatrixMode(GL11.GL_MODELVIEW);
		glLoadIdentity();
	}
	
}
