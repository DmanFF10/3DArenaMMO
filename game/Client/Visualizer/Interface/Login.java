package Client.Visualizer.Interface;

import org.lwjgl.opengl.Display;

import Client.Manager.ICallbacks;
import Client.Visualizer.Interface.Panels.debugPanel;
import Client.Visualizer.Interface.Panels.loginPanel;

import de.matthiasmann.twl.Widget;

public class Login extends Widget {

	final loginPanel loginPanel;
	final debugPanel debugPanel = new debugPanel();
	
	public Login(ICallbacks.visualizerCBs cbs, boolean debugMode) {
		// display size
		int height = (Display.getHeight()/2)-10;
		int width = (Display.getWidth()/2)-10;
	
		debugPanel.setVisible(debugMode);
		debugPanel.setTheme("debug-panel");
		
		loginPanel = new loginPanel(cbs);
		loginPanel.setPosition(width-(loginPanel.getWidth()/2), height-(loginPanel.getHeight()/2));
		
		add(loginPanel);
		add(debugPanel);
	}
	
	public void update(int fps){
		debugPanel.update(fps, 0, 0, 0, 0, 0, 0);
	}
}
