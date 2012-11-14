package Client.Visualizer;

import java.io.File;
import java.io.IOException;

import org.lwjgl.opengl.Display;

import GameLibrary.util.Consts;
import GameLibrary.util.Logger;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;

public class UserInterface {
	
	public static boolean debugMode = false;
	
	
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
		int height = Display.getHeight()/2;
		int width = Display.getWidth()/2;
		
		Widget w = new Widget();
		Button connectButton = new Button("Connect");
		EditField connectTextBox = new EditField();
		Label inputLabel = new Label("InputName:");	
		
		connectButton.setTheme("button");
		connectButton.setSize(65, 30);
		connectButton.setPosition(width-(connectButton.getWidth()/2), height+50);
		
		connectTextBox.setTheme("textBox");
		connectTextBox.setSize(150, 25);
		connectTextBox.setPosition(width-(connectTextBox.getWidth()/2), height);
		
		
		inputLabel.setTheme("label");
		inputLabel.setPosition(width-connectTextBox.getWidth(), height+(connectTextBox.getHeight()/2));
		
		w.add(inputLabel);
		w.add(connectButton);
		w.add(connectTextBox);
		return w;
	}
}
