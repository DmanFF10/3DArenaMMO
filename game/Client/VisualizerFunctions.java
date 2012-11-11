package Client;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import java.io.File;
import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;

import GameLibrary.Consts;
import GameLibrary.Logger;

public class VisualizerFunctions {
	
	private static Properties properties = null;
	
	public static boolean initOpenGL(boolean fullscreen, Properties props){
		properties = props;
		try {
			// create the rendering window
			Display.setDisplayMode(toggleFullscreen(fullscreen));
			Display.setTitle("Arena Graphics Test");
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
		if (fullscreen){
			DisplayMode[] modes = Display.getAvailableDisplayModes();
			Display.setFullscreen(true);
			return modes[0];

			
		} else {
			Display.setFullscreen(false);
			return new DisplayMode(properties.width, properties.height);
		}
	}
	
	public static void enable2D(){
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, properties.width, properties.height, 0, 1, -1);
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
	
	public static LWJGLRenderer initGUIRenderer(){
		LWJGLRenderer renderer;
		try{
			renderer = new LWJGLRenderer();
		} catch(Exception e){
			Logger.log(Logger.ERROR, "failed to load LWJGLRenderer for TWL");
			return null;
		}
		return renderer;
	}
	
	private static GUI initTWL(Widget w){
		return new GUI(w, initGUIRenderer());
	}
	
	public static GUI loadmenus(int menu){
		GUI gui = null;
		try{
			switch(menu){
		    case Consts.GUI_MAIN:
		    	gui = initTWL(mainMenu());		    	
		    	gui.applyTheme(ThemeManager.createThemeManager(
		    			(new File("./game/res/menus/mainTemplate.xml")).toURI().toURL(), initGUIRenderer()));
		    	break;
		    }
		} catch(IOException e){
			Logger.log(Logger.ERROR, "failed to load gui theme");
		}
		return gui;
	}
	
	private static Widget mainMenu() {
		Widget w = new Widget();
		Button button = new Button("Connect");
		Label label = new Label("InputName");
		button.setTheme("button");
		button.setPosition(70, 70);
		button.setSize(65, 35);
		label.setTheme("label");
		label.setPosition(50, 50);
		w.add(label);
		w.add(button);
		return w;
	}
}