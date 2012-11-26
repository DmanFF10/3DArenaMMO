package Client.Visualizer.Interface;

import java.io.File;

import GameLibrary.util.Logger;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;

public class Interface {
	public static GUI createGUI(Widget w, String filename){
		try{
			GUI g = new GUI(w, new LWJGLRenderer());
			g.applyTheme(ThemeManager.createThemeManager(
					(new File("./game/res/menus/"+ filename +".xml")).toURI().toURL(), new LWJGLRenderer()));
			return g;
		} catch(Exception e){
			Logger.log(Logger.ERROR, "failed to create GUI");
			return null;
		}
	}
}