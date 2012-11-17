package Client.Visualizer;

import java.io.File;
import java.io.IOException;

import org.lwjgl.opengl.Display;

import Client.Manager.Callbacks;
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
	public static Callbacks.visualizerCBs callbacks;
	
	
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
		
		objWidth = 65;
		objHeight = 30;
		loginButton.setTheme("button");
		loginButton.setSize(objWidth, objHeight);
		loginButton.setPosition(width-(objWidth/2), height+(objHeight*2));
		
		objWidth = 150;
		objHeight = 20;
		nameTextBox.setTheme("textBox");
		nameTextBox.setSize(objWidth, objHeight);
		nameTextBox.setPosition(width-(objWidth/2), (int)(height-(objHeight*1.5)));
		
		objWidth = 150;
		objHeight = 20;
		passTextBox.setTheme("textBox");
		passTextBox.setSize(objWidth, objHeight);
		passTextBox.setPosition(width-(objWidth/2), height);
		
		objWidth = 5*nameLabel.getText().length();
		nameLabel.setTheme("label");
		nameLabel.setPosition(nameTextBox.getX()-(objWidth+nameTextBox.getWidth()/4), 
				nameTextBox.getY()+(nameTextBox.getHeight()/2));
		
		objWidth = 5*passLabel.getText().length();
		passLabel.setTheme("label");
		passLabel.setPosition(passTextBox.getX()-(objWidth+(passTextBox.getWidth()/4)), 
				passTextBox.getY()+(passTextBox.getHeight()/2));
		
		loginButton.addCallback(new Runnable() {
            public void run() {
                callbacks.connect(nameTextBox.getText(), passTextBox.getText());
            }
        });

		w.add(nameLabel);
		w.add(passLabel);
		w.add(loginButton);
		w.add(nameTextBox);
		w.add(passTextBox);
		return w;
	}
}
